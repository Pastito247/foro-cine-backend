package com.foro_cine.backend.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Por ahora no necesitamos métodos extra.
    // Si más adelante quieres buscar por autor:
    // List<Post> findByAutor(String autor);
}
