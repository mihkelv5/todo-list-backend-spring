package com.todolist.security.manager;

import com.todolist.security.provider.RefreshTokenAuthProvider;
import com.todolist.security.provider.UsernamePasswordAuthProvider;
import com.todolist.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AuthManagerImpl {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailsServiceImpl userDetailService,
                                                       UsernamePasswordAuthProvider usernamePasswordAuthProvider,
                                                       RefreshTokenAuthProvider refreshTokenAuthProvider) throws Exception {


        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
        builder.authenticationProvider(usernamePasswordAuthProvider);
        builder.authenticationProvider(refreshTokenAuthProvider);

        return builder.build();

    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(5);
    }

}
