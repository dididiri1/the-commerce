package sample.thecommerce.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.thecommerce.domain.user.User;

@Getter
@NoArgsConstructor
public class UserCreateResponse {

    private Long userId;

    private String username;

    private String name;

    private String email;

    @Builder
    private UserCreateResponse(Long userId, String username, String name, String email) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public static UserCreateResponse of(User user) {
        return UserCreateResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
