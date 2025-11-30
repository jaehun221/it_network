package com.itnetwork.club_community.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itnetwork.club_community.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member_tbl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    @Id
    private Long uid;

    @Column(name = "USER_ID", nullable = false, unique = true)
    private String user_id;

    @Column(name = "USER_PW", nullable = false)
    private String user_pw;

    @Column(name = "USER_NM", nullable = false, unique = true)
    private String user_nm;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Builder
    public User(Long uid, String user_id, String user_pw, String user_nm, String email) {
        this.uid = uid;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_nm = user_nm;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return user_pw;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-comments")
    private List<Comment> comments = new ArrayList<>();

}
