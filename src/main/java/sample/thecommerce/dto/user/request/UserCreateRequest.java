package sample.thecommerce.dto.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.thecommerce.domain.user.User;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "유저명은 필수입니다.")
    private String username;

    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String tel;

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Builder
    private UserCreateRequest(String username, String password, String nickname, String name, String tel, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.tel = tel;
        this.email = email;
    }

    public User toEntity(String password) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .name(name)
                .tel(tel)
                .email(email)
                .build();
    }
}
