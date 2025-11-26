package com.andromda.shop.service;

import com.andromda.shop.config.JwtService;
import com.andromda.shop.controller.auth.AuthenticationRequest;
import com.andromda.shop.controller.auth.AuthenticationResponse;
import com.andromda.shop.controller.RegisterRequest;
import com.andromda.shop.model.Role;
import com.andromda.shop.model.Usuario;
import com.andromda.shop.repository.UserRepository;
import com.andromda.shop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Role.CLIENTE)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        // DEVUELVE TODOS LOS DATOS
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        // DEVUELVE TODOS LOS DATOS (Incluido el Rol ADMIN)
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol())
                .build();
    }
}
