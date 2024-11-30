package com.banquemisr.challenge05.controller;

import com.banquemisr.challenge05.dto.user.request.LoginRequestDTO;
import com.banquemisr.challenge05.dto.user.request.SignUpRequestDTO;
import com.banquemisr.challenge05.dto.user.response.LoginResponseDTO;
import com.banquemisr.challenge05.service.user.IUserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {
    private final IUserService userService;

    public AuthenticationController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        userService.register(signUpRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User created successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = userService.authenticate(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
