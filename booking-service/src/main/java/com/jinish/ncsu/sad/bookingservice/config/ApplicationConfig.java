package com.jinish.ncsu.sad.bookingservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // This is the only bean this class needs to provide for the booking service.
    @Bean
    public UserDetailsService userDetailsService() {
        // This creates a UserDetails object on-the-fly from the email in the JWT.
        // It does not need a password or roles because we trust the token.
        return username -> new User(username, "", Collections.emptyList());
    }
}