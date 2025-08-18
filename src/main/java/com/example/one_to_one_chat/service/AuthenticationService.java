package com.example.one_to_one_chat.service;

import com.example.one_to_one_chat.dto.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }

    public String authentication(AuthRequest request)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        return jwtService.generateToken(request.username());
    }

}
