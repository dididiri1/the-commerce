package sample.thecommerce.dto.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private Long userId;

    private String username;

    private String nickname;

    private String name;

    private String tel;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDateTime;

    @QueryProjection
    @Builder
    public UserResponse(Long userId, String username, String nickname, String name, String tel, String email, LocalDateTime createDateTime) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.createDateTime = createDateTime;
    }
}
