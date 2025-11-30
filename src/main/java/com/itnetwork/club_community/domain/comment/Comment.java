package com.itnetwork.club_community.domain.comment;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itnetwork.club_community.domain.post.Post;
import com.itnetwork.club_community.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @OnDelete(action = OnDeleteAction.SET_NULL)
    @ManyToOne
    @JsonBackReference("user-comments")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference("post-comments")
    @JoinColumn(name = "post_id")
    private Post post;

    public void update(String content) {
        this.content = content;
    }
}
