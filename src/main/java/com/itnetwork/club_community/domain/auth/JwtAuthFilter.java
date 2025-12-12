package com.itnetwork.club_community.domain.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 클래스
 * 모든 HTTP 요청을 가로채서 JWT 토큰을 검증하고,
 * 유효한 토큰이 있으면 사용자를 인증된 상태로 설정합니다.
 * accessToken이 만료되었을 때 refreshToken을 확인하여 자동으로 새 토큰을 발급합니다.
 *
 * 예시: 사용자가 API를 호출할 때 헤더에 "Authorization: bearer 토큰"을 포함하면,
 * 이 필터가 토큰을 검증하고 인증 정보를 설정합니다.
 * 토큰이 만료되었으면 "Refresh-Token" 헤더를 확인하여 자동으로 갱신합니다.
 */
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    // JWT 토큰을 생성하고 검증하는 Provider
    private final JwtTokenProvider jwtTokenProvider;
    // 리프레시 토큰을 처리하는 Provider
    private final RefreshTokenProvider refreshTokenProvider;
    // 사용자 정보를 로드하는 서비스
    private final UserDetailsService userDetailsService;

    /**
     * 생성자: JWT 토큰 Provider, 리프레시 토큰 Provider, 사용자 서비스를 주입받습니다.
     *
     * @param jwtTokenProvider JWT 토큰을 처리하는 Provider
     * @param refreshTokenProvider 리프레시 토큰을 처리하는 Provider
     * @param userDetailsService 사용자 정보를 가져오는 서비스
     */
    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider, RefreshTokenProvider refreshTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * HTTP 요청을 필터링하여 JWT 토큰을 검증하는 메서드
     * 요청 헤더에서 Authorization 헤더를 확인하고,
     * 유효한 토큰이 있으면 사용자를 인증된 상태로 설정합니다.
     * accessToken이 만료되었을 때 refreshToken이 있으면 자동으로 새 토큰을 발급합니다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 다음 필터로 요청을 전달하는 체인
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 헤더를 가져옵니다
        String header = request.getHeader("Authorization");
        // 리프레시 토큰 헤더를 가져옵니다
        String refreshTokenHeader = request.getHeader("Refresh-Token");

        // 디버깅: 요청 정보 로그 출력
        log.info("🔍 [필터 진입] 요청 경로: {}", request.getRequestURI());
        log.info("🔍 [헤더 확인] Authorization: {}", header != null ? (header.length() > 20 ? header.substring(0, 20) + "..." : header) : "없음");
        log.info("🔍 [헤더 확인] Refresh-Token: {}", refreshTokenHeader != null ? (refreshTokenHeader.length() > 20 ? refreshTokenHeader.substring(0, 20) + "..." : refreshTokenHeader) : "없음");

        boolean authenticated = false;

        // Authorization 헤더가 있고, "bearer" 또는 "Bearer"로 시작하는지 확인합니다 (대소문자 구분 없음)
        if (header != null && header.toLowerCase().startsWith("bearer")) {
            // "bearer " 또는 "Bearer " 부분(7글자)을 제거하고 실제 토큰만 추출합니다
            String token = header.substring(7).trim();

            // 토큰이 유효한지 검증합니다
            boolean isValid = jwtTokenProvider.IsValidate(token);
            log.info("🔍 [토큰 검증] AccessToken 유효성: {}", isValid ? "유효함" : "만료됨 또는 오류");

            if (isValid) {
                // ✅ 토큰이 유효하면 정상적으로 인증 처리 (리프레시 불필요)
                String userId = jwtTokenProvider.GetEmailFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                authenticated = true;
                log.info("✅ [토큰 유효] AccessToken이 아직 유효합니다. 리프레시 불필요.");
            } else {
                // ❌ AccessToken이 만료되었거나 유효하지 않음
                boolean isExpired = jwtTokenProvider.isTokenExpired(token);
                if (isExpired) {
                    log.error("❌ [만료 확인] AccessToken이 만료되었습니다!");
                    log.error("❌ [인증 실패] 만료된 AccessToken으로는 요청할 수 없습니다!");
                    log.info("📋 [만료된 토큰] {}", token);
                    log.warn("💡 [해결 방법] /auth/refresh 엔드포인트를 사용하여 새 AccessToken을 발급받으세요.");
                    // 만료된 토큰은 무조건 인증 실패 (리프레시는 별도 엔드포인트에서만)
                    authenticated = false;
                } else {
                    log.error("❌ [토큰 오류] AccessToken이 유효하지 않습니다 (서명 오류 또는 형식 오류)");
                    authenticated = false;
                }
            }
        } else if (refreshTokenHeader != null && !refreshTokenHeader.isEmpty()) {
            // Authorization 헤더가 없지만 refreshToken이 있는 경우 (리프레시 토큰만으로 인증 시도)
            log.info("⚠️ [토큰 없음] Authorization 헤더가 없지만 RefreshToken이 있습니다. 리프레시 시도...");
            log.info("📋 [리프레시 전] AccessToken: 없음 (Authorization 헤더 없음)");

            if (refreshTokenProvider.IsValidate(refreshTokenHeader)) {
                String email = refreshTokenProvider.GetEmailFromToken(refreshTokenHeader);
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // 새로운 accessToken을 생성합니다
                    String newAccessToken = jwtTokenProvider.GenerateToken(email);

                    // ✅ 리프레시 성공 로그 출력
                    log.info("✅ [리프레시 성공] RefreshToken만으로 새로운 AccessToken이 발급되었습니다!");
                    log.info("📋 [리프레시 후] 새로운 AccessToken: {}", newAccessToken);
                    log.info("👤 사용자: {}", email);

                    // 응답 헤더에 새 토큰을 추가합니다
                    response.setHeader("New-Access-Token", newAccessToken);

                    // 인증 정보를 설정합니다
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    authenticated = true;
                } catch (Exception e) {
                    // 사용자를 찾을 수 없으면 인증 실패
                    log.error("❌ [리프레시 실패] 사용자를 찾을 수 없습니다: {}", email);
                }
            } else {
                log.warn("❌ [리프레시 실패] RefreshToken이 유효하지 않거나 만료되었습니다.");
            }
        } else {
            // 헤더가 아예 없는 경우
            log.warn("⚠️ [인증 실패] Authorization 헤더와 Refresh-Token 헤더가 모두 없습니다.");
        }

        // 인증 상태 로그 출력
        if (authenticated) {
            log.info("✅ [인증 성공] 사용자가 인증되었습니다.");
        } else {
            log.warn("❌ [인증 실패] 인증되지 않은 요청입니다. 403 에러가 발생할 수 있습니다.");
            log.warn("💡 [해결 방법] Authorization 헤더에 'Bearer {토큰}' 또는 Refresh-Token 헤더를 추가하세요.");
        }

        // 다음 필터로 요청을 전달합니다
        filterChain.doFilter(request, response);

    }
}