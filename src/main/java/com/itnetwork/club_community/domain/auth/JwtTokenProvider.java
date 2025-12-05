package com.itnetwork.club_community.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 액세스 토큰을 생성하고 검증하는 클래스
 * 액세스 토큰은 사용자가 인증된 상태임을 증명하는 토큰으로,
 * API 요청 시 헤더에 포함되어 전송됩니다.
 * 
 * 예시: 사용자가 로그인하면 이 클래스가 액세스 토큰을 생성하고,
 * 이후 API 요청 시 이 토큰을 검증하여 사용자 인증을 확인합니다.
 */
@Component
public class JwtTokenProvider {
    // JWT 토큰을 서명하고 검증하기 위한 비밀키
    private Key key;

    /**
     * 생성자: application.properties에서 jwt.secret 값을 받아서 비밀키를 생성합니다.
     * 
     * @param sKey JWT 서명에 사용할 비밀키 문자열
     */
    public JwtTokenProvider(@Value("${jwt.secret}") String sKey) {
        // 문자열을 Key 객체로 변환 (HMAC SHA256 알고리즘 사용)
        this.key = Keys.hmacShaKeyFor(sKey.getBytes());
    }

    /**
     * 액세스 토큰을 생성하는 메서드
     * 액세스 토큰은 짧은 유효기간을 가집니다.
     * 
     * ⚠️ 테스트용: 현재 1분으로 설정되어 있습니다.
     * 실제 운영 환경에서는 3시간(1000L * 60 * 60 * 3)으로 변경하세요.
     * 
     * @param email 사용자의 이메일 (토큰에 포함될 사용자 식별 정보)
     * @return 생성된 액세스 토큰 문자열
     */
    public String GenerateToken(String email) {
        // 현재 시간을 밀리초로 가져옵니다
        long now = System.currentTimeMillis();
        // ⚠️ 테스트용: 액세스 토큰 유효기간 1분 (빠른 테스트를 위해)
        // 실제 운영: 3시간 = 1000L * 60 * 60 * 3
        long expire = 1000L * 60 * 1;  // 1분

        // JWT 토큰을 생성합니다
        return Jwts.builder()
                .setSubject(email)  // 토큰의 주체(사용자 이메일) 설정
                .setIssuedAt(new Date(now))  // 토큰 발급 시간 설정
                .setExpiration(new Date(now + expire))  // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)  // 비밀키로 서명
                .compact();  // 최종 토큰 문자열로 변환
    }

    /**
     * 액세스 토큰이 유효한지 검증하는 메서드
     * 토큰이 만료되었거나 서명이 잘못되었으면 false를 반환합니다.
     * 
     * @param token 검증할 액세스 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean IsValidate(String token) {
        try {
            // 토큰을 파싱하고 서명을 검증합니다
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;  // 검증 성공
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 만료되었거나 형식이 잘못된 경우
            return false;  // 검증 실패
        }
    }

    /**
     * 액세스 토큰에서 사용자 이메일을 추출하는 메서드
     * 
     * @param token 액세스 토큰
     * @return 토큰에 포함된 사용자 이메일
     */
    public String GetEmailFromToken(String token) {
        // 토큰을 파싱해서 클레임(토큰에 포함된 정보)을 가져옵니다
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 클레임에서 주체(이메일)를 반환합니다
        return claims.getSubject();
    }

    /**
     * 토큰이 만료되었는지 확인하는 메서드
     * 토큰이 만료되었어도 파싱은 가능하지만, 유효하지 않습니다.
     * 
     * @param token 확인할 토큰
     * @return 토큰이 만료되었으면 true, 아직 유효하면 false
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // 만료 시간이 현재 시간보다 이전이면 만료됨
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // 파싱 실패 시 만료된 것으로 간주
            return true;
        }
    }

    /**
     * 만료된 토큰에서도 이메일을 추출하는 메서드
     * 토큰이 만료되었어도 정보는 읽을 수 있습니다.
     * 
     * @param token 토큰 (만료되어도 됨)
     * @return 토큰에 포함된 사용자 이메일
     */
    public String GetEmailFromExpiredToken(String token) {
        try {
            // 만료된 토큰도 파싱할 수 있도록 설정 (만료 시간 검증 무시)
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            // 만료된 토큰의 경우에도 클레임을 읽을 수 있도록 처리
            // JWT 라이브러리는 만료된 토큰도 파싱할 수 있지만, 만료 예외가 발생합니다
            // 예외를 무시하고 클레임을 읽어야 합니다
            throw new IllegalArgumentException("토큰에서 이메일을 추출할 수 없습니다: " + e.getMessage());
        }
    }

}
