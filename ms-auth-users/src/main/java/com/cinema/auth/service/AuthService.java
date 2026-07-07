package com.cinema.auth.service;

import com.cinema.auth.dto.AuthRequest;
import com.cinema.auth.dto.AuthResponse;
import com.cinema.auth.entity.User;

public interface AuthService {//Interfaz que define los métodos de autenticación y registro de usuarios, implementada por AuthServiceImpl
    User register(User user);//Método para registrar un nuevo usuario, recibe un objeto User y devuelve el usuario registrado
    AuthResponse login(AuthRequest request);//Método para autenticar un usuario, recibe un objeto AuthRequest con el email y la contraseña del usuario, devuelve un objeto AuthResponse con el token JWT generado y la información del usuario autenticado
}