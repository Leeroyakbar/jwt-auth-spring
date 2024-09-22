package com.raya.neuron.service;

import com.raya.neuron.dto.AuthenticationRequest;
import com.raya.neuron.dto.AuthenticationResponse;
import com.raya.neuron.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
