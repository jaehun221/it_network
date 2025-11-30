package com.itnetwork.club_community.domain.post;


import com.itnetwork.club_community.domain.comment.CommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostListViewResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private String author;

    private List<CommentResponseDto> comments;

    public PostListViewResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContents();
        this.createAt = post.getReg_date();
//        this.author = post.getAuthor();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).toList();
    }

}
