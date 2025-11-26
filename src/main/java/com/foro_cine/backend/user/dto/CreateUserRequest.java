package com.foro_cine.backend.user.dto;

import com.foro_cine.backend.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    private String correo;

    @NotBlank
    private String contrasena; 

    private String ubicacion;
    private String profileImageUrl;
    private UserRole role = UserRole.USUARIO;
}
