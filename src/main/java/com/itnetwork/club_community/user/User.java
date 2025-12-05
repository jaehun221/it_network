package com.itnetwork.club_community.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itnetwork.club_community.comment.Comment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 사용자(회원) 정보를 저장하는 엔티티 클래스
 * 데이터베이스의 member_tbl 테이블과 매핑됩니다.
 * Spring Security의 UserDetails 인터페이스를 구현하여 인증에 사용됩니다.
 * 
 * 예시: 사용자가 회원가입하면 이 클래스의 객체가 데이터베이스에 저장되고,
 * 로그인 시 이 정보를 사용하여 인증을 수행합니다.
 */
@Entity
@Table(name = "member_tbl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    // 사용자 고유 번호 (자동 증가)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    @Id
    private Long uid;

    // 사용자 ID (로그인에 사용되는 고유한 식별자)
    @Column(name = "USER_ID", nullable = false, unique = true)
    private String user_id;

    // 사용자 비밀번호 (암호화되어 저장됨)
    @Column(name = "USER_PW", nullable = false)
    private String user_pw;

    // 사용자 이름 (닉네임)
    @Column(name = "USER_NM", nullable = false, unique = true)
    private String user_nm;

    // 사용자 이메일 (로그인 및 인증에 사용됨)
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    /**
     * 사용자 객체를 생성하는 빌더 패턴 생성자
     * 
     * @param uid 사용자 고유 번호
     * @param user_id 사용자 ID
     * @param user_pw 사용자 비밀번호
     * @param user_nm 사용자 이름
     * @param email 사용자 이메일
     */
    @Builder
    public User(Long uid, String user_id, String user_pw, String user_nm, String email) {
        this.uid = uid;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_nm = user_nm;
        this.email = email;
    }

    /**
     * 사용자의 권한을 반환하는 메서드
     * Spring Security에서 사용자의 권한을 확인할 때 호출됩니다.
     * 
     * @return 사용자의 권한 목록 (현재는 ROLE_USER만 반환)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Spring Security에서 사용하는 사용자 이름을 반환합니다.
     * 이 프로젝트에서는 이메일을 사용자 이름으로 사용합니다.
     * 
     * @return 사용자 이메일
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Spring Security에서 사용하는 비밀번호를 반환합니다.
     * 
     * @return 사용자 비밀번호 (암호화된 상태)
     */
    @Override
    public String getPassword() {
        return user_pw;
    }

    // 사용자가 작성한 댓글 목록 (일대다 관계)
    // 사용자가 삭제되면 관련 댓글도 함께 삭제됩니다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-comments")
    private List<Comment> comments = new ArrayList<>();

}
