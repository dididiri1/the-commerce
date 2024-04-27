package sample.thecommerce.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.thecommerce.domain.user.User;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserCreateResponse {

    private Long id;

    private String username;

    private String name;

    private String email;

    @Builder
    private UserCreateResponse(Long id, String username, String name, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public static UserCreateResponse of(User user) {
        return UserCreateResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
