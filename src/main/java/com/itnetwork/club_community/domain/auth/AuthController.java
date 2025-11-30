package com.itnetwork.club_community.domain.auth;

import com.itnetwork.club_community.domain.user.AddUserRequestDto;
import com.itnetwork.club_community.domain.user.User;
import com.itnetwork.club_community.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    // 인증 로직 컨트롤러
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequestDto request) {
        userService.save(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String pw = request.get("password");


        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));


        if (!bCryptPasswordEncoder.matches(pw, user.getUser_pw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        String token = jwtTokenProvider.GenerateToken(user.getEmail());


        return ResponseEntity.ok(Map.of("token", token));
    }
}
