package com.itnetwork.club_community.domain.comment;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 수정 요청 Dto
 */
@Getter
@Setter
public class CommentUpdateDto {

    private String content; // 수정할 댓글 내용
}
