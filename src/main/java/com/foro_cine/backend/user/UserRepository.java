package com.foro_cine.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar por correo (para registro y login)
    Optional<User> findByCorreo(String correo);
}
