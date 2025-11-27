package com.foro_cine.backend.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Buscar posts por autor (opcional, útil para perfil)
    List<Post> findByAutor(String autor);

    // Buscar posts por userId (más seguro que por nombre)
    List<Post> findByUserId(Long userId);
}
