package com.itnetwork.club_community.domain.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 전달하는 DTO (Data Transfer Object) 클래스
 * 서버에서 클라이언트로 사용자 정보를 전달할 때 사용됩니다.
 * 비밀번호 같은 민감한 정보는 제외하고 공개해도 되는 정보만 포함합니다.
 * 
 * 예시: 사용자 프로필을 조회할 때 비밀번호 없이 사용자 정보를 반환할 때 사용합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInfoDto {
    // 사용자 ID
    private String user_id;
    // 사용자 이름 (닉네임)
    private String user_nm;
    // 사용자 이메일
    private String email;

    /**
     * User 엔티티 객체로부터 UserInfoDto를 생성하는 정적 메서드
     * 엔티티 객체를 DTO로 변환할 때 사용됩니다.
     * 
     * @param user 변환할 User 엔티티 객체
     * @return UserInfoDto 객체
     */
    public static UserInfoDto createUserInfoDto(User user) {
        return new UserInfoDto(
            user.getUser_id(),
            user.getUser_nm(),
            user.getEmail()
        );
    }
}
