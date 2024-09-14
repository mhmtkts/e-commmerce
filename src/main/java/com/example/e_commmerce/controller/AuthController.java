package com.example.e_commmerce.controller;

import com.example.e_commmerce.dto.RegisterResponse;
import com.example.e_commmerce.dto.RegistrationUser;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegistrationUser registrationUser) {
        User createdUser = authenticationService.register(registrationUser.email(), registrationUser.password());
        return new RegisterResponse(createdUser.getEmail(), "Kayıt başarılı bir şekilde gerçekleşti.");
    }
}

