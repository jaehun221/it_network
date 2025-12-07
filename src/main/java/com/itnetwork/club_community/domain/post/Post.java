package com.itnetwork.club_community.domain.post;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itnetwork.club_community.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ******* 임시 ********
 * 게시글(Post) 정보를 저장하는 엔티티 클래스
 * 데이터베이스의 post_tbl 테이블과 매핑됩니다.
 *
 * - 게시글 제목(title), 내용(contents), 조회수(hit_cnt) 등을 저장
 * - 누가, 언제 작성/수정했는지(reg_id, reg_date, upd_id, upd_date) 기록
 * - 하나의 게시글(Post)은 여러 개의 댓글(Comment)을 가질 수 있습니다.
 */
@Entity
@Table(name = "post_tbl") // 실제 테이블명에 맞게 변경 가능
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    // 게시글 고유 번호 (자동 증가 PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id; // 게시물 일련번호

    // 작성자 일련번호 (회원 테이블의 PK를 참조할 수 있는 값)
    @Column(name = "MEMBER_ID", nullable = false)
    private Integer member_id; // 회원 일련번호

    // 게시글 제목
    @Column(name = "TITLE", nullable = false, length = 1000)
    private String title; // 제목

    // 게시글 내용 (긴 텍스트 가능)
    @Lob
    @Column(name = "CONTENTS", nullable = false)
    private String contents; // 내용

    // 조회수 (기본값 0)
    @Column(name = "HIT_CNT", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer hit_cnt; // 조회수 기본값 0

    // 등록자 ID (작성자 ID 문자열 등)
    @Column(name = "REG_ID", nullable = false, length = 15)
    private String reg_id; // 등록자

    // 등록 일시 (INSERT 시 자동 세팅)
    @CreationTimestamp
    @Column(name = "REG_DATE", nullable = false)
    private LocalDateTime reg_date; // 등록일

    // 수정자 ID
    @Column(name = "UPD_ID", length = 15)
    private String upd_id; // 수정자

    // 수정 일시 (UPDATE 시 자동 갱신)
    @UpdateTimestamp
    @Column(name = "UPD_DATE")
    private LocalDateTime upd_date; // 수정일

    /**
     * 게시글에 달린 댓글 목록
     *
     * - Post(1) : Comment(N) 관계의 양방향 매핑
     * - mappedBy = "post"  → Comment 엔티티의 'post' 필드와 연결
     * - 게시글이 삭제될 경우, 관련 댓글도 함께 삭제 (cascade = REMOVE)
     * - @OrderBy("id asc")  → 댓글 ID 오름차순 정렬
     *
     * JsonManagedReference("post-comments")
     *  - 직렬화 시 순환 참조(무한 루프)를 방지하기 위해 사용
     *  - 반대쪽(Comment)에서는 JsonBackReference("post-comments") 사용
     */
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference("post-comments")
    @OrderBy("id asc")
    private List<Comment> comments = new ArrayList<>();

    /**
     * 게시글 객체를 생성하는 빌더 패턴 생성자
     *
     * @param id          게시글 고유 번호
     * @param member_id   회원 일련번호
     * @param title       제목
     * @param contents    내용
     * @param hit_cnt     조회수
     * @param reg_id      등록자
     * @param reg_date    등록일
     * @param upd_id      수정자
     * @param upd_date    수정일
     */
    @Builder
    public Post(Long id, Integer member_id, String title, String contents, Integer hit_cnt,
                String reg_id, LocalDateTime reg_date, String upd_id, LocalDateTime upd_date) {
        this.id = id;
        this.member_id = member_id;
        this.title = title;
        this.contents = contents;
        this.hit_cnt = hit_cnt;
        this.reg_id = reg_id;
        this.reg_date = reg_date;
        this.upd_id = upd_id;
        this.upd_date = upd_date;
    }
}
