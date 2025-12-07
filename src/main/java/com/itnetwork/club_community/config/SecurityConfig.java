package com.itnetwork.club_community.config;

import com.itnetwork.club_community.domain.auth.JwtAuthFilter;
import com.itnetwork.club_community.domain.auth.JwtTokenProvider;
import com.itnetwork.club_community.domain.auth.RefreshTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Security 설정 클래스
 * 애플리케이션의 보안 설정을 관리합니다.
 * JWT 인증 필터, 비밀번호 인코더, CORS 설정 등을 구성합니다.
 * 
 * 예시: 사용자가 API를 호출할 때 어떤 경로는 인증이 필요하고,
 * 어떤 경로는 인증 없이 접근할 수 있는지 여기를 설정합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * 비밀번호 암호화를 위한 BCrypt 인코더를 생성하는 Bean
     * 비밀번호를 해시화하여 저장할 때 사용됩니다.
     * 
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT 인증 필터를 생성하는 Bean
     * 모든 HTTP 요청을 가로채서 JWT 토큰을 검증합니다.
     * accessToken이 만료되었을 때 refreshToken으로 자동 갱신합니다.
     * 
     * @param jwtTokenProvider JWT 토큰을 처리하는 Provider
     * @param refreshTokenProvider 리프레시 토큰을 처리하는 Provider
     * @param userDetailsService 사용자 정보를 가져오는 서비스
     * @return JwtAuthFilter 객체
     */
    @Bean
    public JwtAuthFilter jwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenProvider refreshTokenProvider,
            UserDetailsService userDetailsService) {
        return new JwtAuthFilter(jwtTokenProvider, refreshTokenProvider, userDetailsService);
    }


    /**
     * Spring Security의 보안 필터 체인을 설정하는 Bean
     * 어떤 경로에 어떤 권한이 필요한지, CSRF 보호, 세션 관리 등을 설정합니다.
     * 
     * @param http HttpSecurity 객체 (보안 설정을 구성하는 객체)
     * @param jwtAuthenticationFilter JWT 인증 필터
     * @return SecurityFilterChain 객체
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (JWT 사용 시 필요 없음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 사용 안 함 (JWT는 무상태)
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 경로
                        .requestMatchers("/", "/login", "/auth/signup", "/auth/login", "/auth/refresh", "/api/**").permitAll()
                        // 리프레시 토큰 테스트용 엔드포인트는 인증 필요
                        .requestMatchers("/auth/test").authenticated()
                        // ADMIN 권한이 필요한 경로
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // ADMIN 또는 USER 권한이 필요한 경로
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // 그 외 모든 경로는 인증 필요
                        .anyRequest().authenticated())
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * CORS (Cross-Origin Resource Sharing) 설정
     * 프론트엔드와 백엔드가 다른 도메인에서 실행될 때 통신을 허용하기 위한 설정입니다.
     * 
     * @param registry CORS 설정을 등록하는 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOrigins("http://localhost:5173")  // 프론트엔드 도메인 (예: React 개발 서버)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*");  // 모든 헤더 허용
    }
}
