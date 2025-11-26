package com.foro_cine.backend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String titulo;
    private String contenido;
    private String autor;
    private String fecha;
    private int likes;
    private int dislikes;
    private int userVote; // -1 dislike, 0 nada, 1 like
}
