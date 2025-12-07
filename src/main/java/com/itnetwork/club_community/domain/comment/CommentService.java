package com.itnetwork.club_community.domain.comment;

import com.itnetwork.club_community.domain.post.Post;
import com.itnetwork.club_community.domain.post.PostRepository;
import com.itnetwork.club_community.domain.user.User;
import com.itnetwork.club_community.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글(Comment)에 대한 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * - 댓글 작성, 수정, 삭제
 * - 게시글별 댓글 목록 조회
 * - 작성자 검증(본인만 수정/삭제)
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 로그인 이메일(String)을 기반으로 User 엔티티를 조회하는 헬퍼 메서드
     */
    private User getUserByEmail(String email) {
        if (email == null) {
            throw new RuntimeException("로그인 후 이용할 수 있습니다.");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }

    /**
     * 댓글 생성
     *
     * @param dto   작성 요청 DTO
     * @param email 로그인된 사용자 이메일 (JwtAuthFilter 에서 넣어준 principal)
     */
    @Transactional
    public void createComment(CommentCreateDto dto, String email) {

        // 이메일로 실제 User 엔티티 조회
        User user = getUserByEmail(email);

        // 댓글이 달릴 게시글 조회
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(user)     // 인증된 실제 사용자
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     *
     * @param commentId 수정할 댓글 ID
     * @param dto       수정 요청 데이터
     * @param email     로그인한 사용자 이메일
     */
    @Transactional
    public void updateComment(Long commentId, CommentUpdateDto dto, String email) {

        User loginUser = getUserByEmail(email);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        // 본인이 작성한 댓글인지 검증 (uid 기준)
        if (!comment.getUser().getUid().equals(loginUser.getUid())) {
            throw new RuntimeException("본인 댓글만 수정할 수 있습니다.");
        }

        comment.updateContent(dto.getContent());
        // JPA 변경 감지로 자동 반영, save() 생략해도 되지만 유지해도 무방
        commentRepository.save(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     * @param email     로그인한 사용자 이메일
     */
    @Transactional
    public void deleteComment(Long commentId, String email) {

        User loginUser = getUserByEmail(email);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        // 본인 여부 확인 (uid 기준)
        if (!comment.getUser().getUid().equals(loginUser.getUid())) {
            throw new RuntimeException("본인 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    /**
     * 특정 게시글의 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {

        List<Comment> comments = commentRepository.findByPostIdOrderByIdAsc(postId);

        return comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .regDate(comment.getReg_date())
                        .updDate(comment.getUpd_date())
                        .writerUid(comment.getUser().getUid())
                        .writerId(comment.getUser().getUser_id())
                        .writerName(comment.getUser().getUser_nm())
                        .build())
                .collect(Collectors.toList());
    }
}
