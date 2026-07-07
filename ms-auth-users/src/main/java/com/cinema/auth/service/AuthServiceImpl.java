package com.cinema.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cinema.auth.dto.AuthRequest;
import com.cinema.auth.dto.AuthResponse;
import com.cinema.auth.entity.User;
import com.cinema.auth.repository.UserRepository;
import com.cinema.auth.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User register(User user) {
        // Validar si el correo ya está registrado
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }
        
        // ENCRIPTAR LA CONTRASEÑA antes de guardarla en Postgres
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Por defecto, si no viene rol, le asignamos CLIENTE
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("CLIENTE");
        } else {
            user.setRole(user.getRole().toUpperCase());
        }
        
        return userRepository.save(user);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        // 1. Buscar al usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas o usuario no encontrado."));

        // 2. Comparar la contraseña en texto plano con el hash encriptado de la BD
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas.");
        }

        // 3. Si todo está bien, generar el token JWT e incluir su Rol
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }
}