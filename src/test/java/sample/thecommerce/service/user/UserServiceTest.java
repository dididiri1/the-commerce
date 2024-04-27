package sample.thecommerce.service.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.thecommerce.IntegrationTestSupport;
import sample.thecommerce.domain.user.User;
import sample.thecommerce.domain.user.UserQueryRepository;
import sample.thecommerce.domain.user.UserRepositoryJpa;
import sample.thecommerce.dto.user.request.UserCreateRequest;
import sample.thecommerce.dto.user.response.UserCreateResponse;
import sample.thecommerce.dto.user.response.UserResponse;
import sample.thecommerce.handler.ex.validationException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @AfterEach
    void tearDown() {
        userRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("신규 회원을 등록 한다.")
    @Test
    void createMember() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .email("test@example.com")
                .build();

        //when
        UserCreateResponse response = userService.createUser(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("test");
        assertThat(response.getName()).isEqualTo("홍길동");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
    }

    @DisplayName("중복된 사용자 이름으로 회원가입할 경우 예외가 발생한다.")
    @Test
    void createUserWithDuplicateUsername() throws Exception {
        //given
        User user1 = createUser("testUser", "홍길동", "test@example.com");
        userRepositoryJpa.save(user1);

        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .email("test@example.com")
                .build();

        //when //then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(validationException.class)
                .hasMessage("이미 사용 중인 유저명입니다.");
    }

    @DisplayName("회원 목록 1페이지를 조회한다.")
    @Test
    void getUsersWithPage1() throws Exception {
        //given
        List<User> requestPosts = IntStream.range(1, 20)
                .mapToObj(i -> User.builder()
                        .username("member" + i)
                        .password(new BCryptPasswordEncoder().encode("1234"))
                        .nickname("nickname")
                        .name("name" + i)
                        .tel("02123123")
                        .email("test1@example.com")
                        .build())
                .collect(Collectors.toList());

        userRepositoryJpa.saveAll(requestPosts);

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<UserResponse> result = userService.getUsers(pageRequest);

        //then
        assertThat(result).hasSize(3)
                .extracting("username", "name")
                .containsExactlyInAnyOrder(
                        tuple("member1", "name1"),
                        tuple("member2", "name2"),
                        tuple("member3", "name3")
                );
        assertThat(result.isFirst()).isTrue();
        assertThat(result.hasNext()).isTrue();
    }

    @DisplayName("회원 목록 1개를 조회한다.")
    @Test
    void getUsersWithSize1() throws Exception {
        //given
        List<User> requestPosts = IntStream.range(1, 20)
                .mapToObj(i -> User.builder()
                        .username("member" + i)
                        .password(new BCryptPasswordEncoder().encode("1234"))
                        .nickname("nickname")
                        .name("name" + i)
                        .tel("02123123")
                        .email("test1@example.com")
                        .build())
                .collect(Collectors.toList());

        userRepositoryJpa.saveAll(requestPosts);

        PageRequest pageRequest = PageRequest.of(1, 1);

        //when
        Page<UserResponse> result = userService.getUsers(pageRequest);

        //then
        assertThat(result).hasSize(1);
    }

    @DisplayName("회원 목록를 조회한다. 정렬은 이름순 내림차순이다.")
    @Test
    void getUsersWithPage0AndOrderByNameDesc() throws Exception {
        //given
        User user1 = createUser("member1", "name1", "test1@example.com");
        User user2 = createUser("member2", "name2", "test2@example.com");
        User user3 = createUser("member3", "name3", "test3@example.com");
        User user4 = createUser("member4", "name4", "test4@example.com");
        User user5 = createUser("member5", "name5", "test5@example.com");
        userRepositoryJpa.saveAll(List.of(user1, user2, user3, user4, user5));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Page<UserResponse> result = userService.getUsers(pageRequest);

        //then
        assertThat(result).hasSize(3)
                .extracting("username", "name")
                .containsExactlyInAnyOrder(
                        tuple("member5", "name5"),
                        tuple("member4", "name4"),
                        tuple("member3", "name3")
                );
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
