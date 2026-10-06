package com.everfolgar.fastorder.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.everfolgar.fastorder.dto.AuthResponse;
import com.everfolgar.fastorder.dto.LoginRequest;
import com.everfolgar.fastorder.dto.RegisterRequest;
import com.everfolgar.fastorder.model.Rol;
import com.everfolgar.fastorder.model.Usuario;
import com.everfolgar.fastorder.repository.UsuarioRepository;
import com.everfolgar.fastorder.security.CustomUserDetailsService;
import com.everfolgar.fastorder.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Usuario u = Usuario.builder()
                .nombre(req.getNombre())
                .direccion(req.getDireccion())
                .telefono(req.getTelefono())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .rol(Rol.CLIENTE)
                .build();
        usuarioRepository.save(u);
        UserDetails ud = userDetailsService.loadUserByUsername(u.getEmail());
        return new AuthResponse(jwtService.generateToken(ud, u.getRol().name()), u.getEmail(), u.getRol().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        Usuario u = usuarioRepository.findByEmail(req.getEmail()).orElseThrow();
        UserDetails ud = userDetailsService.loadUserByUsername(u.getEmail());
        return new AuthResponse(jwtService.generateToken(ud, u.getRol().name()), u.getEmail(), u.getRol().name());
    }
}
