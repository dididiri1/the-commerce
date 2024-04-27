package sample.thecommerce.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sample.thecommerce.domain.BaseEntity;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    private String username;

    @Column(length = 250)
    private String password;

    private String nickname;

    private String name;

    private String tel;

    private String email;

    @Builder
    public User(Long id, String username, String password, String nickname, String name, String tel, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.tel = tel;
        this.email = email;
    }
}
