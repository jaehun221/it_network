package com.itnetwork.club_community.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 데이터를 데이터베이스에서 조회하고 저장하는 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 자동으로 제공받습니다.
 * 
 * 예시: 이메일로 사용자를 찾거나, 사용자를 저장/삭제할 때 이 리포지토리를 사용합니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 이메일로 사용자를 찾는 메서드
     * 
     * @param email 찾을 사용자의 이메일
     * @return 사용자 정보 (Optional: 사용자가 없을 수도 있음)
     */
    Optional<User> findByEmail(String email);
}
