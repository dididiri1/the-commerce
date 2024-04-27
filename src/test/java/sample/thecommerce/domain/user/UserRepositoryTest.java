package sample.thecommerce.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.thecommerce.IntegrationTestSupport;
import sample.thecommerce.dto.user.response.UserResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
public class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @DisplayName("회원 목록을 조회한다.")
    @Test
    void searchPageUsers() throws Exception {
        //given
        User user1 = createUser("user1", "name1", "test1@example.com");
        User user2 = createUser("user2", "name2", "test2@example.com");
        User user3 = createUser("user3", "name3", "test3@example.com");
        User user4 = createUser("user4", "name4", "test4@example.com");
        userRepositoryJpa.saveAll(List.of(user1, user2, user3, user4));

        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Page<UserResponse> users = userQueryRepository.searchPageUsers(pageRequest);

        //then
        assertThat(users).hasSize(4)
                .extracting("username", "name", "email")
                .containsExactlyInAnyOrder(
                        tuple("user4", "name4", "test4@example.com"),
                        tuple("user3", "name3", "test3@example.com"),
                        tuple("user2", "name2", "test2@example.com"),
                        tuple("user1", "name1", "test1@example.com")
                );
    }

    @DisplayName("회원을 수정 한다.")
    @Test
    void updateUser() throws Exception {
        //given
        User user = createUser("user1", "name1", "test1@example.com");
        userRepositoryJpa.save(user);

        //when
        User findUser = userRepositoryJpa.findById(user.getId()).orElse(null);
        findUser.setEmail("test2@example.com");
        findUser.setName("김구라");
        findUser.setNickname("춘식이");

        //then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo("test2@example.com");
        assertThat(findUser.getName()).isEqualTo("김구라");
    }

    private User createUser(String username, String name, String email) {
        return User.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode("1234"))
                .nickname("닉네임")
                .name(name)
                .tel("02123123")
                .email(email)
                .build();
    }


}
