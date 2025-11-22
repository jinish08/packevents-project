package com.jinish.ncsu.sad.userservice.config;

import com.jinish.ncsu.sad.userservice.model.Role;
import com.jinish.ncsu.sad.userservice.model.User;
import com.jinish.ncsu.sad.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create an admin user if one doesn't exist
        if (userRepository.findByEmail("admin@packevents.com").isEmpty()) {
            User adminUser = new User();
            adminUser.setName("Admin");
            adminUser.setEmail("admin@packevents.com");
            adminUser.setPassword(passwordEncoder.encode("adminpass"));
            adminUser.setRole(Role.ROLE_ADMIN);
            userRepository.save(adminUser);
            log.info("--- Admin user seeded ---");
        }
    }
}