package com.example.dizserver.service;


import com.example.dizserver.config.JwtUtil;
import com.example.dizserver.dto.AuthenticateRequire;
import com.example.dizserver.dto.AuthenticateResponse;
import com.example.dizserver.model.User;
import com.example.dizserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class AuthenticateService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }

    public AuthenticateResponse createToken(AuthenticateRequire authenticateRequire) {
        User user = userRepository.findByEmail(authenticateRequire.getEmail()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + authenticateRequire.getEmail()));

        if (!passwordEncoder.matches(authenticateRequire.getPassword(), user.getPassword())) {
            throw new NoSuchElementException("Password is wrong: " + authenticateRequire.getEmail());
        }

        AuthenticateResponse authenticateResponse = new AuthenticateResponse();
        authenticateResponse.setToken(jwtUtil.generateToken(user.getUserName()));
        authenticateResponse.setId(user.getId());

        return authenticateResponse;
    }


}
