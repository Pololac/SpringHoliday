package com.hb.cda.springholiday.security;

import com.hb.cda.springholiday.security.jwt.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private AuthenticationConfiguration authenticationConfiguration;
    private JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain accessControl (HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((req) -> req
                    .requestMatchers("/api/protected").authenticated()  // AUTHENTIFIE
                    .requestMatchers("/api/bookings").authenticated()
                    .requestMatchers("/api/account").authenticated()
                    .requestMatchers("/api/admin").hasRole( "ADMIN")    // GESTION VIA LES ROLES
                    .anyRequest().permitAll())
            .csrf((csrf) -> csrf.disable());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager getManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
