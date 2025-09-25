package com.jinish.ncsu.sad.userservice.controller;

import com.jinish.ncsu.sad.userservice.dto.LoginRequest;
import com.jinish.ncsu.sad.userservice.model.User;
import com.jinish.ncsu.sad.userservice.repository.UserRepository;
import com.jinish.ncsu.sad.userservice.dto.LoginResponse;
import com.jinish.ncsu.sad.userservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // Import this
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) { // Add this
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Add this
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // Passwords match, generate a token
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new LoginResponse(token)); // Return the token
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        // Spring Security stores the authenticated user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
}
