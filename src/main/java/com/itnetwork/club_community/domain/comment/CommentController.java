package com.itnetwork.club_community.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글(Comment) 관련 REST API를 제공하는 컨트롤러 클래스입니다.
 *
 * - 댓글 작성
 * - 댓글 수정
 * - 댓글 삭제
 * - 게시글별 댓글 목록 조회
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 게시글의 댓글 목록 조회
     *
     * GET /api/comments?postId=1
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam("postId") Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 작성
     *
     * POST /api/comments
     * body: { "postId": 1, "content": "댓글 내용" }
     */
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentCreateDto dto,
                                              @AuthenticationPrincipal String email) {

        // JwtAuthFilter 에서 principal 로 email(String)을 넣었기 때문에
        // 여기서 String 으로 바로 받는다.
        System.out.println(">>> [DEBUG] createComment email = " + email);

        commentService.createComment(dto, email);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정
     *
     * PATCH /api/comments/{commentId}
     * body: { "content": "수정된 내용" }
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentUpdateDto dto,
                                              @AuthenticationPrincipal String email) {

        System.out.println(">>> [DEBUG] updateComment email = " + email);

        commentService.updateComment(commentId, dto, email);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 삭제
     *
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal String email) {

        System.out.println(">>> [DEBUG] deleteComment email = " + email);

        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok().build();
    }
}
