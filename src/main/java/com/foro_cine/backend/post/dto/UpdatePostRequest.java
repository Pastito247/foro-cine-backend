package com.foro_cine.backend.post.dto;

import lombok.Data;

@Data
public class UpdatePostRequest {

    private String title;
    private String content;
    private String category;
    private String movieTitle;
}
