package com.itnetwork.club_community.repository;

import com.itnetwork.club_community.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //DB 접근 담당
public interface BoardRepository extends JpaRepository<Board, Long> {
    // JpaRepository 상속으로 CRUD 기능 자동 생성
}