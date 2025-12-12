package com.itnetwork.club_community.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Comment 엔티티에 대한 데이터베이스 접근을 담당하는 Repository 인터페이스입니다.
 *
 * - Spring Data JPA에서 제공하는 JpaRepository를 상속하여,
 *   기본적인 CRUD 메서드(findAll, findById, save, delete 등)를 자동으로 제공합니다.
 *
 * - 추가로, 게시글별 댓글 목록 조회, 사용자별 댓글 조회와 같은
 *   커스텀 조회 메서드를 정의할 수 있습니다.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글에 달린 댓글 목록을 ID 오름차순으로 조회합니다.
     *
     * @param postId 조회할 게시글의 ID (Post 엔티티의 PK)
     * @return 해당 게시글에 달린 댓글 목록 (ID 오름차순 정렬)
     *
     * 메서드 이름 규칙 설명:
     *  - findByPostIdOrderByIdAsc
     *    → Comment.post.id 값을 기준으로 검색
     *    → Spring Data JPA가 post.id 경로를 자동으로 인식합니다.
     */
    List<Comment> findByPostIdOrderByIdAsc(Long postId);

    /**
     * 특정 사용자(회원)가 작성한 댓글 목록을 ID 내림차순으로 조회합니다.
     *
     * @param uid 조회할 회원의 고유 번호 (User.uid)
     * @return 해당 사용자가 작성한 댓글 목록 (ID 내림차순 정렬)
     *
     * 메서드 이름 규칙 설명:
     *  - findByUserUidOrderByIdDesc
     *    → Comment.user.uid 값을 기준으로 검색
     *    → User 엔티티의 필드명(uid)에 맞춰서 네이밍했습니다.
     */
    List<Comment> findByUserUidOrderByIdDesc(Long uid);
}
