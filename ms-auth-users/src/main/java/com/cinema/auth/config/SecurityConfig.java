package com.cinema.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Clase de configuración de seguridad para Spring Security, define cómo se manejará la autenticación y autorización en el microservicio
@Configuration// Indica que esta clase contiene beans de configuración para Spring
@EnableWebSecurity// Habilita la seguridad web de Spring Security
public class SecurityConfig {

    @Bean// Bean para el encriptador de contraseñas, se usará para encriptar las contraseñas de los usuarios antes de guardarlas en la base de datos
    public PasswordEncoder passwordEncoder() {//passwordEncoder() es un método que devuelve un objeto PasswordEncoder, que es una interfaz de Spring Security para encriptar contraseñas
        return new BCryptPasswordEncoder(); // Encriptador estándar de la industria
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF porque usaremos REST y JWT
            .authorizeHttpRequests(auth -> auth//authorizeHttpRequests() es un método que permite configurar las reglas de autorización para las solicitudes HTTP entrantes
                .requestMatchers("/api/v1/auth/**").permitAll() // Permitimos libre acceso a registro y login
                .anyRequest().authenticated() // Cualquier otra ruta pedirá autenticación
            );
        
        return http.build();//devuelve el objeto SecurityFilterChain construido con las configuraciones definidas
    }
}