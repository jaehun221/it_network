package com.itnetwork.club_community.domain.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInfoDto {
    private String user_id;
    private String user_nm;
    private String email;

    public static UserInfoDto createUserInfoDto(User user) {
        return new UserInfoDto(
            user.getUser_id(),
            user.getUser_nm(),
            user.getEmail()
        );
    }
}
