package com.itnetwork.club_community.comment;

import com.itnetwork.club_community.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentByPostOrderById(Post post);
    Optional<Comment> findByPostIdAndId(Long postId, Long id);
}
