package com.itnetwork.club_community.domain.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itnetwork.club_community.domain.post.Post;
import com.itnetwork.club_community.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 댓글(Comment) 정보를 저장하는 엔티티 클래스
 * 데이터베이스의 comment_tbl 테이블과 매핑됩니다.
 *
 * - 어떤 게시글(Post)에 달렸는지(post)
 * - 누가(User) 작성했는지(user)
 * - 내용(content), 등록일(reg_date), 수정일(upd_date)을 관리합니다.
 */
@Entity
@Table(name = "comment_tbl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    /**
     * 댓글 고유 번호 (PK, 자동 증가)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    /**
     * 댓글 내용
     */
    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    /**
     * 댓글 등록일 (INSERT 시 자동 입력)
     */
    @CreationTimestamp
    @Column(name = "REG_DATE", nullable = false, updatable = false)
    private LocalDateTime reg_date;

    /**
     * 댓글 수정일 (UPDATE 시 자동 갱신)
     */
    @UpdateTimestamp
    @Column(name = "UPD_DATE")
    private LocalDateTime upd_date;

    /**
     * 댓글 작성자(User)
     *
     * - 여러 댓글(N)이 하나의 User(1)에 속함 (N:1)
     * - JoinColumn USER_ID → member_tbl의 ID(uid) 참조
     * - 직렬화 순환 참조 방지를 위해 JsonBackReference 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonBackReference("user-comments")
    private User user;

    /**
     * 댓글이 달린 게시글(Post)
     *
     * - 여러 댓글(N)이 하나의 Post(1)에 속함 (N:1)
     * - JoinColumn POST_ID → post_tbl의 ID 참조
     * - 직렬화 순환 참조 방지를 위해 JsonBackReference 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    @JsonBackReference("post-comments")
    private Post post;

    /**
     * 댓글 내용 수정 메서드
     */
    public void updateContent(String content) {
        this.content = content;
    }
}
