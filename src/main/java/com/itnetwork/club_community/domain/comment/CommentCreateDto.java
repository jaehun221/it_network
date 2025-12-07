package com.itnetwork.club_community.domain.comment;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 작성 요청 데이터를 전달하는 Dto입니다.
 *
 * - 어떤 게시글(postId)에
 * - 어떤 내용(content)을 작성하는지 전달합니다.
 *
 * 작성자는 Controller에서 @AuthenticationPrincipal User 로 처리되므로
 * 별도로 writerId 등을 받을 필요가 없습니다.
 */
@Getter
@Setter
public class CommentCreateDto {

    // 댓글을 작성할 게시글 ID
    private Long postId;

    // 댓글 내용
    private String content;
}
