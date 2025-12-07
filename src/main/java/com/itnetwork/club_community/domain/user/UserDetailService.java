package com.itnetwork.club_community.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security에서 사용하는 사용자 정보를 로드하는 서비스 클래스
 * JWT 인증 필터에서 사용자 정보를 가져올 때 이 서비스를 사용합니다.
 * 
 * 예시: JWT 토큰에서 이메일을 추출한 후, 이 서비스를 통해 사용자 정보를 가져옵니다.
 */
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    // 사용자 정보를 조회하는 서비스
    private final UserService userService;

    /**
     * 이메일(사용자 이름)로 사용자 정보를 로드하는 메서드
     * Spring Security가 인증 과정에서 이 메서드를 호출합니다.
     * 
     * @param email 사용자 이메일 (Spring Security에서는 username으로 사용됨)
     * @return 사용자 정보 (UserDetails 타입)
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일로 사용자를 찾고, 없으면 예외를 발생시킵니다
        return userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }
}
