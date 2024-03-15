package com.example.timemanagementapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.timemanagementapp.dto.request.LoginRequestDTO;
import com.example.timemanagementapp.dto.request.RegisterRequestDTO;
import com.example.timemanagementapp.dto.response.JwtReponseDTO;
import com.example.timemanagementapp.dto.transfer.UserDetailsTransferDTO;
import com.example.timemanagementapp.security.JwtUtil;
import com.example.timemanagementapp.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthorizationController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    public AuthorizationController(UserService userService, AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtReponseDTO> login(@RequestBody LoginRequestDTO LoginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(LoginRequestDTO.getUsername(), LoginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(new JwtReponseDTO(jwt, LoginRequestDTO.getUsername()));

    }

    @PostMapping("/register")
    public ResponseEntity<JwtReponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {

        userService
                .createUser(
                        new UserDetailsTransferDTO(registerRequestDTO.getUsername(), registerRequestDTO.getPassword()));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequestDTO.getUsername(),
                        registerRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(new JwtReponseDTO(jwt, registerRequestDTO.getUsername()));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("registeredfsadfd");
    }
}
