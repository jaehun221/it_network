package com.itnetwork.club_community.comment;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt = LocalDateTime.now();
    private String nickname;
    private Long userId;
    private Long postId;

//    entity -> dto
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedDate();
        this.updateAt = comment.getUpdatedDate();
        this.postId = comment.getPost().getId();
        this.nickname = comment.getUser().getUser_nm();
        this.userId = comment.getUser().getUid();

    }
}
