package com.itnetwork.club_community.domain.comment;


import com.itnetwork.club_community.domain.post.Post;
import com.itnetwork.club_community.domain.post.PostRepository;
import com.itnetwork.club_community.domain.user.User;
import com.itnetwork.club_community.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 추가
    public Comment save(Long id, AddCommentRequest request, String user_nm) {
        Optional<User> userOptional = userRepository.findByEmail(user_nm);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            System.out.println("사용자가 존재하지 않습니다: " + user_nm);
            return null;
        }
        Post post = postRepository.findById(Math.toIntExact(id)).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패: 해당 게시글이 존재하지 않습니다." + id));

        request.setUser(user);
        request.setPost(post);

        return commentRepository.save(request.toEntity());
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> findAll(Long id) {
        Post post = postRepository.findById(Math.toIntExact(id)).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));
        List<Comment> comments = post.getComments();
        return comments;
    }

    // 댓글 업데이트
    @Transactional
    public void update(Long postId, Long id, UpdateCommentRequestDto dto) {
        Comment comment = commentRepository.findByPostIdAndId(postId, id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));

        comment.update(dto.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long postId, Long id) {
        Comment comment = commentRepository.findByPostIdAndId(postId, id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));

        commentRepository.delete(comment);
    }

    // TODO: Post에 작성자 이름 필드가 존재하지 않음
    // 게시글을 작성한 유저인지 확인
//    private static void authorrizedPostAuthor(Post post) {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (!post.getReg_id().equals(userName)) {
//
//        }
//    }
}
