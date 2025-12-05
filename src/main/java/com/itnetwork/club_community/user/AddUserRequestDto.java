package com.itnetwork.club_community.user;


import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청 정보를 담는 DTO (Data Transfer Object) 클래스
 * 클라이언트에서 서버로 회원가입 정보를 전달할 때 사용됩니다.
 * 
 * 예시: 사용자가 회원가입 폼에 입력한 정보를 이 클래스의 객체로 받아서 처리합니다.
 */
@Getter
@Setter
public class AddUserRequestDto {
    // 사용자 ID (로그인에 사용되는 고유한 식별자)
    private String user_id;
    // 사용자 비밀번호 (서버에서 암호화되어 저장됨)
    private String user_pw;
    // 사용자 이름 (닉네임)
    private String user_nm;
    // 사용자 이메일 (로그인 및 인증에 사용됨)
    private String email;

}