package com.foro_cine.backend.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

    Optional<PostVote> findByPostIdAndUserId(Long postId, Long userId);

    List<PostVote> findByUserId(Long userId);

    List<PostVote> findByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostVote v WHERE v.postId = :postId")
    void deleteByPostId(Long postId);
}
