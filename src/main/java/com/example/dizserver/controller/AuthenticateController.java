package com.example.dizserver.controller;

import com.example.dizserver.config.JwtUtil;
import com.example.dizserver.dto.AuthenticateRequire;
import com.example.dizserver.dto.AuthenticateResponse;
import com.example.dizserver.dto.Register;
import com.example.dizserver.model.User;
import com.example.dizserver.service.AuthenticateService;
import com.example.dizserver.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/authenticate")
@Validated
public class AuthenticateController {

    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    JwtUtil jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponse> createUser(@Valid @RequestBody AuthenticateRequire authenticateRequire) {
        return new ResponseEntity<>(authenticateService.createToken(authenticateRequire), HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody Register register, Errors errors) {
        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.createUser(register), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public String  createUser( @RequestBody String token) {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.extractUsername(token);
            return jwtUtils.generateToken(username);
        } else {
            throw new RuntimeException("Invalid JWT token");
        }
    }

}
