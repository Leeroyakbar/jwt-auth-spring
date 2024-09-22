package com.raya.neuron.service.impl;

import com.raya.neuron.dto.AuthenticationRequest;
import com.raya.neuron.dto.AuthenticationResponse;
import com.raya.neuron.dto.RegisterRequest;
import com.raya.neuron.entity.Role;
import com.raya.neuron.entity.Users;
import com.raya.neuron.repository.UserRepository;
import com.raya.neuron.service.AuthenticationService;
import com.raya.neuron.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        Users user = Users.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Users user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
