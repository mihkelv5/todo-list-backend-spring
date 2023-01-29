package com.todolist.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.todolist.ToDoListBackendApplication;
import com.todolist.constant.SecurityConstant;
import com.todolist.controller.AuthController;
import com.todolist.email.EmailServiceImpl;
import com.todolist.entity.UserModel;
import com.todolist.service.UserDetailsServiceImpl;
import com.todolist.service.UserService;
import com.todolist.util.JwtUtil;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ToDoListBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAuthenticationTests {

    @LocalServerPort
    private int port;
    private GreenMail testSmtp;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    JwtUtil jwtUtil = new JwtUtil();


    String cookie = "";
    String username = "username";
    String password = "password";
    String email = "email";

    @BeforeAll
    void setUpAll(){
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();
    }

    @BeforeEach
    void setUp() {

    }


    @Test
    @Order(1)
    public void shouldRegisterNewUserAndNotExistingUser(){
        //setup
        HashMap<String, String> user = new HashMap<>();
        user.put("username", this.username);
        user.put("email", this.email);
        user.put("password", this.password);

        //query
        ResponseEntity<?> registerResponseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/auth/register", user, UserModel.class);

        //assert
        assertEquals(HttpStatus.CREATED, registerResponseEntity.getStatusCode());

    }

    @Test
    @Order(2)
    public void shouldNotRegisterExistingUser() {
        HashMap<String, String> user = new HashMap<>();
        user.put("username", this.username);
        user.put("email", this.email);
        user.put("password", this.password);

        //query
        ResponseEntity<?> registerResponseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/auth/register", user, String.class);


        assertNotEquals(HttpStatus.OK, registerResponseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    public void shouldNotActivateUser() {
        //setup
        UUID uuid = UUID.randomUUID();

        //query
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://127.0.0.1:" + port + "/auth/activate?username=" +
                        this.username + "&code=" + uuid, String.class);

        //assert
        assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }





    @Test
    @Order(4)
    public void shouldActivateUser() throws MessagingException, IOException {
        //setup
        Message[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);

        String url = messages[0].getContent().toString();
        String regex = "(?<=code=)[^&]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String code = "";
        if(matcher.find()){
            code = matcher.group(0);
        }

        //query
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://127.0.0.1:" + port + "/auth/activate?username=" +
                        this.username + "&code=" + code, String.class);

        //assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(5)
    public void shouldGenerateRefreshToken() {
        //setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("username", this.username);
        headers.add("password", this.password);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //query
        ResponseEntity<?> loginResponseEntity = this.restTemplate
                .exchange("http://127.0.0.1:" + port + "/auth/login", HttpMethod.GET, entity, String.class);

        this.cookie = loginResponseEntity.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        //assert
        assertEquals(HttpStatus.OK, loginResponseEntity.getStatusCode());
        assertTrue(this.cookie.contains("Refresh-Token"));
    }

    @Test
    @Order(6)
    public void shouldNotGenerateRefreshToken() {
        //setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("username", this.username+"a");
        headers.add("password", this.password);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //query
        ResponseEntity<?> loginResponseEntity = this.restTemplate
                .exchange("http://127.0.0.1:" + port + "/auth/login", HttpMethod.GET, entity, String.class);

        //assert
        assertNotEquals(HttpStatus.OK, loginResponseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    public void shouldGenerateAccessToken() {
        //setup
        HttpHeaders headers = new HttpHeaders();
        headers.add("username", this.username);

        headers.add(HttpHeaders.COOKIE, this.cookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //query
        ResponseEntity<?> accessResponseEntity = this.restTemplate
                .exchange("http://127.0.0.1:" + port + "/auth/get-access", HttpMethod.GET, entity, String.class);
        String token = accessResponseEntity.getHeaders().get(SecurityConstant.JWT_TOKEN_HEADER).get(0);

        //assert
        assertEquals(HttpStatus.OK, accessResponseEntity.getStatusCode());
        assertEquals(jwtUtil.getSubject(token), "username");
    }

    @Test
    @Order(8)
    public void shouldNotGenerateAccessToken() {
        //setup
        HttpCookie httpCookie  = ResponseCookie.from(HttpHeaders.COOKIE, UUID.randomUUID().toString())
                .httpOnly(true)
                .secure(false)
                .domain("127.0.0.1")
                .path("/")
                .maxAge(1000)
                .sameSite("Lax")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("username", this.username);

        headers.add(HttpHeaders.COOKIE, httpCookie.toString());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //query
        ResponseEntity<?> accessResponseEntity = this.restTemplate
                .exchange("http://127.0.0.1:" + port + "/auth/get-access", HttpMethod.GET, entity, String.class);


        //assert
        assertNotEquals(HttpStatus.OK, accessResponseEntity.getStatusCode());

    }





}
