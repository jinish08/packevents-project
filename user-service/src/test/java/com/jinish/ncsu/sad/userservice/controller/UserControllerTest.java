package com.jinish.ncsu.sad.userservice.controller;

import com.jinish.ncsu.sad.userservice.dto.LoginRequest;
import com.jinish.ncsu.sad.userservice.model.Role;
import com.jinish.ncsu.sad.userservice.model.User;
import com.jinish.ncsu.sad.userservice.repository.UserRepository;
import com.jinish.ncsu.sad.userservice.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    // Helper method to create LoginRequest since no constructor exists
    private LoginRequest createLoginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    // ==========================================
    // PART 1: BLACK BOX TESTING (Equivalence Class Partitioning)
    // ==========================================

    @Test
    @DisplayName("BB-1: Valid Credentials (Class: Valid) -> Returns 200 OK")
    void bb_validCredentials() {
        // Arrange
        LoginRequest request = createLoginRequest("valid@ncsu.edu", "pass123");

        User user = new User();
        user.setEmail("valid@ncsu.edu");
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("valid-token");

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("BB-2: Invalid Password (Class: Invalid) -> Returns 401 Unauthorized")
    void bb_wrongPassword() {
        // Arrange
        LoginRequest request = createLoginRequest("valid@ncsu.edu", "wrongpass");

        User user = new User();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    @DisplayName("BB-3: Non-Existent User (Class: Invalid) -> Returns 401 Unauthorized")
    void bb_userNotFound() {
        // Arrange
        LoginRequest request = createLoginRequest("unknown@ncsu.edu", "pass123");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("BB-4: Null Email Field (Class: Invalid Data Format) -> Returns 401 Unauthorized")
    void bb_nullEmail() {
        // Arrange
        LoginRequest request = createLoginRequest(null, "pass123");
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("BB-5: Empty Password Field (Class: Invalid Data Format) -> Returns 401 Unauthorized")
    void bb_emptyPassword() {
        // Arrange
        LoginRequest request = createLoginRequest("valid@ncsu.edu", "");

        User user = new User();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("", "encodedPass")).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // ==========================================
    // PART 2: WHITE BOX TESTING (Control Flow/Logic Coverage)
    // ==========================================

    @Test
    @DisplayName("WB-1: Flow - User Missing Branch (Stop execution early)")
    void wb_flow_userMissing() {
        // Logic: If user is empty, verify PasswordEncoder is NEVER called.
        LoginRequest request = createLoginRequest("unknown@ncsu.edu", "pass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        userController.loginUser(request);

        // Assert Internal Flow
        verify(userRepository, times(1)).findByEmail(any());
        verifyNoInteractions(passwordEncoder); // Ensure flow stopped before password check
        verifyNoInteractions(jwtService);      // Ensure flow stopped before token generation
    }

    @Test
    @DisplayName("WB-2: Flow - Password Mismatch Branch (Stop execution middle)")
    void wb_flow_passwordMismatch() {
        // Logic: User found, but password check fails. verify JwtService NEVER called.
        LoginRequest request = createLoginRequest("valid@ncsu.edu", "wrong");
        User user = new User();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        userController.loginUser(request);

        // Assert Internal Flow
        verify(passwordEncoder, times(1)).matches(any(), any());
        verifyNoInteractions(jwtService); // Ensure flow stopped before token generation
    }

    @Test
    @DisplayName("WB-3: Flow - Success Branch (Full Execution)")
    void wb_flow_success() {
        // Logic: All checks pass. verify generateToken IS called.
        LoginRequest request = createLoginRequest("valid@ncsu.edu", "pass");
        User user = new User();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userController.loginUser(request);

        // Assert Internal Flow
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("WB-4: Data Flow - Verify correct user object passed to Token Generator")
    void wb_dataFlow_tokenGenerationArgs() {
        // Logic: Ensure the user object retrieved from DB is the EXACT one passed to JWT service
        LoginRequest request = createLoginRequest("valid@ncsu.edu", "pass");
        User dbUser = new User();
        dbUser.setEmail("specific@ncsu.edu");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userController.loginUser(request);

        // Assert that generateToken was called with our specific dbUser object
        verify(jwtService).generateToken(dbUser);
    }

    @Test
    @DisplayName("WB-5: Data Flow - Verify Password Encoder receives raw and encoded passwords")
    void wb_dataFlow_passwordEncoderArgs() {
        // Logic: Ensure the raw password from request and encoded password from DB
        // are passed to the matcher in the correct order.
        String rawPass = "rawPassword123";
        String dbPass = "hashXYZ";

        LoginRequest request = createLoginRequest("valid@ncsu.edu", rawPass);
        User user = new User();
        user.setPassword(dbPass);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        userController.loginUser(request);

        // Assert specific arguments passed to internal dependency
        verify(passwordEncoder).matches(eq(rawPass), eq(dbPass));
    }
}