package com.foro_cine.backend.post.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private Long userId;
    private int vote; // -1, 0, 1
}
