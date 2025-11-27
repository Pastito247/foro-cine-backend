package com.foro_cine.backend.post;

import com.foro_cine.backend.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;

    // ✅ Obtener todos los posts (sin userVote)
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        List<PostDto> dtos = posts.stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getTitulo(),
                        post.getContenido(),
                        post.getAutor(),
                        post.getFecha(),
                        post.getLikes(),
                        post.getDislikes(),
                        0 // userVote por ahora 0
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ✅ Crear post (likes/dislikes siempre en 0 al inicio)
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        post.setLikes(0);
        post.setDislikes(0);
        return ResponseEntity.ok(postRepository.save(post));
    }

    // ✅ Votar like/dislike usando QUERY PARAMS (como lo hace la app)
    @PostMapping("/{postId}/vote")
    public ResponseEntity<PostDto> vote(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestParam int vote // -1, 0, 1
    ) {
        if (vote < -1 || vote > 1) {
            return ResponseEntity.badRequest().build();
        }

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        // Buscar si el usuario ya votó antes
        PostVote existing = postVoteRepository
                .findByPostIdAndUserId(postId, userId)
                .orElse(null);

        int previousVote = (existing != null) ? existing.getVote() : 0;
        int newVote = vote;

        // Quitar el voto anterior de los contadores
        if (previousVote == 1) {
            post.setLikes(post.getLikes() - 1);
        } else if (previousVote == -1) {
            post.setDislikes(post.getDislikes() - 1);
        }

        // Aplicar el nuevo voto
        if (newVote == 1) {
            post.setLikes(post.getLikes() + 1);
        } else if (newVote == -1) {
            post.setDislikes(post.getDislikes() + 1);
        }

        // Manejar el registro en post_votes
        if (existing == null) {
            if (newVote != 0) {
                existing = PostVote.builder()
                        .postId(postId)
                        .userId(userId)
                        .vote(newVote)
                        .build();
                postVoteRepository.save(existing);
            }
        } else {
            if (newVote == 0) {
                // quitar completamente el voto
                postVoteRepository.delete(existing);
            } else {
                existing.setVote(newVote);
                postVoteRepository.save(existing);
            }
        }

        postRepository.save(post);

        PostDto dto = new PostDto(
                post.getId(),
                post.getTitulo(),
                post.getContenido(),
                post.getAutor(),
                post.getFecha(),
                post.getLikes(),
                post.getDislikes(),
                newVote // userVote
        );

        return ResponseEntity.ok(dto);
    }
}
