package sample.thecommerce.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.thecommerce.domain.user.User;

@Getter
@NoArgsConstructor
public class UserUpdateResponse {

    private Long userId;

    private String username;

    private String name;

    private String email;

    private String nickname;

    @Builder
    private UserUpdateResponse(Long userId, String username, String name, String email, String nickname) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserUpdateResponse of(User user) {
        return UserUpdateResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
