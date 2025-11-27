package com.foro_cine.backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    // ✅ Obtener comentarios de un post
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // ✅ Crear comentario para un post
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        // id lo genera la BD, se limpia por seguridad
        comment.setId(null);
        // el postId lo tomamos de la ruta
        comment.setPostId(postId);

        Comment saved = commentRepository.save(comment);
        return ResponseEntity.ok(saved);
    }

    // ✅ Eliminar comentario por id
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!commentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
