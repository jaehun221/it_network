package com.itnetwork.club_community.domain.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {
    private final CommentService commentService;
//    private final String TEST_EMAIL = "rnlrhd78@gmail.com";  // 테스트용

    // 댓글 생성
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<Comment> save(@PathVariable Long id, @RequestBody AddCommentRequest request, Principal principal) {
        Comment savedComment = commentService.save(id, request, principal.getName());
//        Comment savedComment = commentService.save(id, request, TEST_EMAIL);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedComment);
    }

    // 댓글 조회
    @GetMapping("/posts/{id}/comments")
    public List<CommentResponseDto> read(@PathVariable long id) {
        return commentService.findAll(id).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 업데이트
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<Long> update(@PathVariable long postId, @PathVariable Long id, @RequestBody UpdateCommentRequestDto dto) {
        commentService.update(postId, id, dto);

        return ResponseEntity.ok(id);
    }

    // 댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<Long> delete(@PathVariable long postId, @PathVariable Long id) {
        commentService.delete(postId, id);
        return ResponseEntity.ok(id);
    }
}
