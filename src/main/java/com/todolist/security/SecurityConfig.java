
package com.todolist.security;


import com.todolist.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceImpl userDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }



    @Bean
    SecurityFilterChain web(HttpSecurity http, Filter jwtRequestFilter) throws Exception {
        http.csrf().disable().cors().and()
                .authorizeRequests(authorize ->
                authorize
                        .antMatchers("/api/user/register").permitAll()
                        .antMatchers("/api/task/**").hasAnyRole("USER")
                        .mvcMatchers("/api/event/find/{eventId}").access("@webSecurity.checkIfUserInEvent(authentication, #eventId)")
                        .antMatchers("/api/event/**").hasAnyRole("USER")
                        .antMatchers("/api/user/login").permitAll().anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(5);
    }

}
