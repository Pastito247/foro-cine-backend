package com.foro_cine.backend.post;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "post_votes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * -1 = dislike
     *  0 = sin voto
     *  1 = like
     */
    @Column(nullable = false)
    private int vote;
}
