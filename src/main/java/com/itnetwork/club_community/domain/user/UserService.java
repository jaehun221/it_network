package com.itnetwork.club_community.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long save(AddUserRequestDto request) {
        User user = User.builder()
                .user_id(request.getUser_id())
                .user_pw(bCryptPasswordEncoder.encode(request.getUser_pw())) // 암호화
                .user_nm(request.getUser_nm())
                .email(request.getEmail())
                .build();

        return userRepository.save(user).getUid();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
