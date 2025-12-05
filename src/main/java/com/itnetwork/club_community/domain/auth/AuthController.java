package com.itnetwork.club_community.domain.auth;

import com.itnetwork.club_community.user.AddUserRequestDto;
import com.itnetwork.club_community.user.User;
import com.itnetwork.club_community.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 인증 관련 API를 처리하는 컨트롤러
 * 회원가입, 로그인 등의 인증 기능을 제공합니다.
 * 
 * 예시: 사용자가 "/auth/login"으로 이메일과 비밀번호를 보내면,
 * 이 컨트롤러가 인증을 처리하고 JWT 토큰을 발급합니다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    // 사용자 정보를 관리하는 서비스
    private final UserService userService;
    // 비밀번호를 암호화하고 검증하는 인코더
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // JWT 액세스 토큰을 생성하는 Provider
    private final JwtTokenProvider jwtTokenProvider;
    // 리프레시 토큰을 생성하는 Provider
    private final RefreshTokenProvider refreshTokenProvider;


    /**
     * 회원가입 API
     * 새로운 사용자를 등록합니다.
     * 
     * @param request 회원가입 요청 정보 (이메일, 비밀번호, 이름 등)
     * @return 회원가입 성공 메시지
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequestDto request) {
        // 사용자 정보를 저장합니다 (비밀번호는 자동으로 암호화됩니다)
        userService.save(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 로그인 API
     * 사용자의 이메일과 비밀번호를 확인하고, 
     * 인증이 성공하면 JWT 액세스 토큰과 리프레시 토큰을 발급합니다.
     * 
     * @param request 로그인 요청 정보 (이메일, 비밀번호)
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        // 요청에서 이메일과 비밀번호를 추출합니다
        String email = request.get("email");
        String pw = request.get("password");

        // 이메일로 사용자를 찾습니다 (없으면 예외 발생)
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 입력한 비밀번호와 저장된 암호화된 비밀번호를 비교합니다
        if (!bCryptPasswordEncoder.matches(pw, user.getUser_pw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 인증이 성공했으므로 액세스 토큰과 리프레시 토큰을 생성합니다
        String accessToken = jwtTokenProvider.GenerateToken(user.getEmail());
        String refreshToken = refreshTokenProvider.GenerateRefreshToken(user.getEmail());

        // 액세스 토큰과 리프레시 토큰을 응답으로 반환합니다
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    /**
     * 리프레시 토큰으로 새로운 액세스 토큰을 발급하는 API
     * 액세스 토큰이 만료되었을 때 리프레시 토큰을 사용해서 새 액세스 토큰을 받을 수 있습니다.
     * 
     * @param request 리프레시 토큰이 포함된 요청
     * @return 새로운 액세스 토큰
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        // 요청에서 리프레시 토큰을 추출합니다
        String refreshToken = request.get("refreshToken");

        // 리프레시 토큰이 없으면 예외 발생
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("리프레시 토큰이 필요합니다.");
        }

        // 리프레시 토큰이 유효한지 검증합니다
        if (!refreshTokenProvider.IsValidate(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 리프레시 토큰에서 사용자 이메일을 추출합니다
        String email = refreshTokenProvider.GetEmailFromToken(refreshToken);

        // 사용자가 존재하는지 확인합니다
        userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 새로운 액세스 토큰을 생성합니다
        String newAccessToken = jwtTokenProvider.GenerateToken(email);

        // 새로운 액세스 토큰을 응답으로 반환합니다
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    /**
     * 리프레시 토큰 작동 확인용 테스트 API
     * 인증이 필요한 엔드포인트로, accessToken이 만료되었을 때 
     * refreshToken으로 자동 갱신되는지 실시간으로 확인할 수 있습니다.
     * 
     * 응답 헤더에 "New-Access-Token"이 있으면 자동 리프레시가 발생한 것입니다.
     * 
     * @return 인증 성공 메시지와 현재 사용자 정보
     */
    @GetMapping("/test")
    public ResponseEntity<?> testAuth() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : "unknown";
        
        return ResponseEntity.ok(Map.of(
                "message", "인증 성공! 리프레시 토큰이 정상 작동합니다.",
                "status", "success",
                "user", email,
                "note", "응답 헤더에 'New-Access-Token'이 있으면 자동 리프레시가 발생했습니다!"
        ));
    }
}
