package com.cinema.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {//Clase que representa la solicitud de autenticación, contiene el email y la contraseña del usuario que intenta autenticarse
    private String email;//email del usuario que intenta autenticarse
    private String password;//contraseña del usuario que intenta autenticarse
}