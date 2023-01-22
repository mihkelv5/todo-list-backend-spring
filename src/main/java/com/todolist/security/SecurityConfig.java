
package com.todolist.security;


import com.todolist.security.filter.JwtRequestFilter;
import com.todolist.security.filter.RefreshTokenFilter;
import com.todolist.security.filter.UsernamePasswordAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{


    @Bean
    SecurityFilterChain web(HttpSecurity http, JwtRequestFilter jwtRequestFilter,
                            UsernamePasswordAuthFilter usernamePasswordAuthFilter,
                            RefreshTokenFilter refreshTokenFilter) throws Exception {
        return http.csrf().disable()
                .cors().and()
                .authorizeHttpRequests(authorize ->
                    authorize
                        .requestMatchers("/auth/**", "/auth/activate/**").permitAll() //had to specify /activate separately, otherwise it still did not allow access
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(usernamePasswordAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(refreshTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
