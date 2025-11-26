package com.foro_cine.backend.user;

import com.foro_cine.backend.user.dto.CreateUserRequest;
import com.foro_cine.backend.user.dto.LoginRequest;
import com.foro_cine.backend.user.dto.LoginResponse;
import com.foro_cine.backend.user.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // ⭐ Listar todos
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // ⭐ Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ⭐ Registrar usuario
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserRequest request) {
        // Validar correo único
        if (userRepository.findByCorreo(request.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User user = User.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .contrasena(hashPassword(request.getContrasena()))
                .ubicacion(request.getUbicacion())
                .profileImageUrl(request.getProfileImageUrl())
                .role(UserRole.USUARIO) // default
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    // ⭐ Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return userRepository.findById(id)
                .map(existing -> {

                    if (request.getNombre() != null)
                        existing.setNombre(request.getNombre());

                    if (request.getCorreo() != null)
                        existing.setCorreo(request.getCorreo());

                    if (request.getUbicacion() != null)
                        existing.setUbicacion(request.getUbicacion());

                    if (request.getProfileImageUrl() != null)
                        existing.setProfileImageUrl(request.getProfileImageUrl());

                    return ResponseEntity.ok(userRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ⭐ Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ⭐ Login por correo + contraseña
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userRepository.findByCorreo(request.getCorreo())
                .filter(user -> user.getContrasena().equals(hashPassword(request.getContrasena())))
                .map(user -> ResponseEntity.ok(
                        new LoginResponse(
                                user.getId(),
                                user.getNombre(),
                                user.getCorreo(),
                                user.getUbicacion(),
                                user.getProfileImageUrl(),
                                user.getRole()
                        )
                ))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // 🔐 Hash ultra simple para explicar en la defensa
    private String hashPassword(String raw) {
        // SOLO para la materia, NO para producción
        return new StringBuilder(raw).reverse().toString();
    }
}
