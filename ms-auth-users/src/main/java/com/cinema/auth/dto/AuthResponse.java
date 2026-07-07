package com.cinema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {//Clase que representa la respuesta de autenticación, contiene el token JWT generado y la información del usuario autenticado
    private String token;//token JWT generado para el usuario autenticado
    private String email;//email del usuario autenticado
    private String role;//rol del usuario autenticado, puede ser "USER" o "ADMIN"
}