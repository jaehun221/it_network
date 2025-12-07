package com.itnetwork.club_community.domain.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 댓글 조회 응답 Dto
 */
@Getter
@Builder
public class CommentResponseDto {

    private Long id;               // 댓글 ID
    private String content;        // 댓글 내용
    private LocalDateTime regDate; // 작성일
    private LocalDateTime updDate; // 수정일

    private Long writerUid;        // 작성자 UID
    private String writerId;       // 작성자 로그인 ID
    private String writerName;     // 작성자 닉네임
}
