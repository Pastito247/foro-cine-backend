package com.foro_cine.backend.post;

import com.foro_cine.backend.post.dto.PostDto;
import com.foro_cine.backend.post.dto.VoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;

    // ✅ Obtener todos los posts (sin userVote → 0)
    @GetMapping
    public List<PostDto> getAll(
            @RequestParam(required = false) Long userId
    ) {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> {
                    int userVote = 0;
                    if (userId != null) {
                        userVote = postVoteRepository
                                .findByPostIdAndUserId(post.getId(), userId)
                                .map(PostVote::getVote)
                                .orElse(0);
                    }
                    return new PostDto(
                            post.getId(),
                            post.getTitulo(),
                            post.getContenido(),
                            post.getAutor(),
                            post.getFecha(),
                            post.getLikes(),
                            post.getDislikes(),
                            userVote
                    );
                })
                .toList();
    }

    // ✅ Crear post (likes/dislikes siempre en 0 al inicio)
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        post.setLikes(0);
        post.setDislikes(0);
        return ResponseEntity.ok(postRepository.save(post));
    }

    // ✅ Votar like/dislike
    @PostMapping("/{postId}/vote")
    public ResponseEntity<PostDto> vote(
            @PathVariable Long postId,
            @RequestBody VoteRequest request
    ) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getVote() < -1 || request.getVote() > 1) {
            return ResponseEntity.badRequest().build();
        }

        PostVote existing = postVoteRepository
                .findByPostIdAndUserId(postId, request.getUserId())
                .orElse(null);

        int previousVote = existing != null ? existing.getVote() : 0;
        int newVote = request.getVote();

        // Ajustar contadores según cambio
        if (previousVote == 1) post.setLikes(post.getLikes() - 1);
        if (previousVote == -1) post.setDislikes(post.getDislikes() - 1);

        if (newVote == 1) post.setLikes(post.getLikes() + 1);
        if (newVote == -1) post.setDislikes(post.getDislikes() + 1);

        // Guardar cambios
        if (existing == null) {
            existing = PostVote.builder()
                    .postId(postId)
                    .userId(request.getUserId())
                    .vote(newVote)
                    .build();
        } else {
            existing.setVote(newVote);
        }

        postVoteRepository.save(existing);
        postRepository.save(post);

        // Devolver PostDto con userVote actualizado
        PostDto dto = new PostDto(
                post.getId(),
                post.getTitulo(),
                post.getContenido(),
                post.getAutor(),
                post.getFecha(),
                post.getLikes(),
                post.getDislikes(),
                newVote
        );

        return ResponseEntity.ok(dto);
    }
}
