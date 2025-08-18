package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.dto.AuthRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {


    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtService = Mockito.mock(JwtService.class);
        authenticationService = new AuthenticationService(authenticationManager, jwtService);
    }

    @Test
    void whenAuthenticationCalledWithValidCredentials_itShouldReturnToken() {
        AuthRequest request = new AuthRequest("username", "1234");

        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Mockito.when(jwtService.generateToken("username")).thenReturn("mockedToken");

        String token = authenticationService.authentication(request);

        Assertions.assertEquals("mockedToken", token);

        Mockito.verify(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtService).generateToken("username");
    }

    @Test
    void whenAuthenticationFails_itShouldThrowException() {
        AuthRequest request = new AuthRequest("username", "wrongPassword");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        Assertions.assertThrows(RuntimeException.class, () -> authenticationService.authentication(request));

        Mockito.verify(jwtService, Mockito.never()).generateToken(Mockito.anyString());
    }

    @AfterEach
    void tearDown() {
    }
}