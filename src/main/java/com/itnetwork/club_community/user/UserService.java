package com.itnetwork.club_community.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 사용자 등록, 조회 등의 기능을 제공합니다.
 * 
 * 예시: 회원가입 시 이 서비스가 사용자 정보를 저장하고,
 * 로그인 시 이메일로 사용자를 찾아서 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    // 사용자 데이터를 데이터베이스에 저장하고 조회하는 리포지토리
    private final UserRepository userRepository;
    // 비밀번호를 암호화하는 인코더
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 새로운 사용자를 등록하는 메서드
     * 비밀번호는 자동으로 암호화되어 저장됩니다.
     * 
     * @param request 회원가입 요청 정보 (이메일, 비밀번호, 이름 등)
     * @return 저장된 사용자의 고유 번호(uid)
     */
    @Transactional
    public Long save(AddUserRequestDto request) {
        // 요청 정보로 사용자 객체를 생성합니다
        User user = User.builder()
                .user_id(request.getUser_id())  // 사용자 ID
                .user_pw(bCryptPasswordEncoder.encode(request.getUser_pw()))  // 비밀번호 암호화
                .user_nm(request.getUser_nm())  // 사용자 이름
                .email(request.getEmail())  // 이메일
                .build();

        // 데이터베이스에 저장하고 저장된 사용자의 고유 번호를 반환합니다
        return userRepository.save(user).getUid();
    }

    /**
     * 이메일로 사용자를 찾는 메서드
     * 로그인 시 사용자의 이메일로 사용자 정보를 조회할 때 사용됩니다.
     * 
     * @param email 찾을 사용자의 이메일
     * @return 사용자 정보 (Optional: 사용자가 없을 수도 있음)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
