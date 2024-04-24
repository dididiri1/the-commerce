package sample.thecommerce.dto.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.thecommerce.domain.user.User;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "유저명 타입은 필수입니다.")
    private String username;

    @NotBlank(message = "패스워드 타입은 필수입니다.")
    private String password;

    @NotBlank(message = "닉네임 타입은 필수입니다.")
    private String nickname;

    @NotBlank(message = "이름 타입은 필수입니다.")
    private String name;

    @NotBlank(message = "핸드폰번호 타입은 필수입니다.")
    private String phone;

    @NotBlank(message = "이메일 타입은 필수입니다.")
    private String email;

    @Builder
    private UserCreateRequest(String username, String password, String nickname, String name, String phone, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public User toEntity(String password) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .name(name)
                .phone(phone)
                .email(email)
                .build();
    }
}
