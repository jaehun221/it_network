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
 * 리프레시 토큰을 생성하고 검증하는 클래스
 * 리프레시 토큰은 액세스 토큰보다 긴 유효기간을 가지며,
 * 액세스 토큰이 만료되었을 때 새로운 액세스 토큰을 발급받기 위해 사용됩니다.
 *
 * 예시: 사용자가 로그인하면 액세스 토큰(3시간)과 리프레시 토큰(7일)을 받습니다.
 * 액세스 토큰이 만료되면 리프레시 토큰을 사용해서 새로운 액세스 토큰을 받을 수 있습니다.
 */
@Component
public class RefreshTokenProvider {
    // JWT 토큰을 서명하고 검증하기 위한 비밀키
    private Key key;

    /**
     * 생성자: application.properties에서 jwt.secret 값을 받아서 비밀키를 생성합니다.
     *
     * @param sKey JWT 서명에 사용할 비밀키 문자열
     */
    public RefreshTokenProvider(@Value("${jwt.secret}") String sKey) {
        // 문자열을 Key 객체로 변환 (HMAC SHA256 알고리즘 사용)
        this.key = Keys.hmacShaKeyFor(sKey.getBytes());
    }

    /**
     * 리프레시 토큰을 생성하는 메서드
     * 액세스 토큰보다 긴 유효기간을 가집니다.
     *
     * ⚠️ 테스트용: 현재 30분으로 설정되어 있습니다.
     * AccessToken(1분)이 만료되어도 RefreshToken으로 리프레시할 수 있도록 충분히 길게 설정했습니다.
     * 실제 운영 환경에서는 7일(1000L * 60 * 60 * 24 * 7)로 변경하세요.
     *
     * @param email 사용자의 이메일 (토큰에 포함될 사용자 식별 정보)
     * @return 생성된 리프레시 토큰 문자열
     */
    public String GenerateRefreshToken(String email) {
        // 현재 시간을 밀리초로 가져옵니다
        long now = System.currentTimeMillis();
        // ⚠️ 테스트용: 리프레시 토큰 유효기간 30분 (AccessToken 1분 만료 후에도 리프레시 가능하도록)
        // 실제 운영: 7일 = 1000L * 60 * 60 * 24 * 7
        long expire = 1000L * 60 * 30;  // 30분

        // JWT 토큰을 생성합니다
        return Jwts.builder()
                .setSubject(email)  // 토큰의 주체(사용자 이메일) 설정
                .setIssuedAt(new Date(now))  // 토큰 발급 시간 설정
                .setExpiration(new Date(now + expire))  // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)  // 비밀키로 서명
                .compact();  // 최종 토큰 문자열로 변환
    }

    /**
     * 리프레시 토큰이 유효한지 검증하는 메서드
     * 토큰이 만료되었거나 서명이 잘못되었으면 false를 반환합니다.
     *
     * @param token 검증할 리프레시 토큰
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
     * 리프레시 토큰에서 사용자 이메일을 추출하는 메서드
     *
     * @param token 리프레시 토큰
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
}

