package com.algotutor.messenger.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.JwtResponse;
import com.algotutor.messenger.dto.LoginRequest;
import com.algotutor.messenger.dto.RegisterRequest;
import com.algotutor.messenger.dto.UserDTO;
import com.algotutor.messenger.entities.User;
import com.algotutor.messenger.exception.UserAlreadyExistsException;
import com.algotutor.messenger.repos.UserRepository;
import com.algotutor.messenger.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDTO registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }

        User user = new User(request.getUsername(), request.getEmail(), 
                           passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    public JwtResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(user);

        return new JwtResponse(jwt, new UserDTO(user));
    }
}