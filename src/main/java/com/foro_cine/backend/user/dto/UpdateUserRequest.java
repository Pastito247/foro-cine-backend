package com.foro_cine.backend.user.dto;

import com.foro_cine.backend.user.UserRole;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String nombre;
    private String correo;
    private String contrasena;
    private String ubicacion;
    private String profileImageUrl;
    private UserRole role;
}
