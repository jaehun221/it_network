package com.itnetwork.club_community.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글(Post) 엔티티에 대한 데이터베이스 접근을 담당하는 Repository 인터페이스입니다.
 *
 * - Spring Data JPA의 JpaRepository<Post, Long> 를 상속하여
 *   기본적인 CRUD 메서드(findAll, findById, save, delete 등)를 자동으로 제공합니다.
 *
 * - 현재는 추가 메서드 없이 기본 기능만 사용하지만,
 *   나중에 제목 검색, 카테고리별 조회 등 커스텀 메서드를 정의할 수 있습니다.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
