package com.foro_cine.backend.user.dto;

import com.foro_cine.backend.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String ubicacion;
    private String profileImageUrl;
    private UserRole role;
}
