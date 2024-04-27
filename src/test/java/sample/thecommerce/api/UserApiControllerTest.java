package sample.thecommerce.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import sample.thecommerce.ControllerTestSupport;
import sample.thecommerce.dto.user.request.UserCreateRequest;
import sample.thecommerce.dto.user.response.UserResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserApiControllerTest extends ControllerTestSupport {

    @DisplayName("신규 회원을 등록한다.")
    @Test
    void createUser() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password("1234")
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .email("test@example.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("신규 회원 등록할 때 유저명은 필수값이다.")
    @Test
    void createUserWithoutUsername() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .password("1234")
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .email("test@naver.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("유저명은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("신규 회원 등록할 때 패스워드는 필수값이다.")
    @Test
    void createUserWithoutPassword() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .email("test@example.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("패스워드는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("신규 회원 등록할 때 닉네임은 필수값이다.")
    @Test
    void createUserWithoutNickname() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password("123")
                .name("홍길동")
                .tel("02123123")
                .email("test@example.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("신규 회원 등록할 때 이름은 필수값이다.")
    @Test
    void createUserWithoutName() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password("123")
                .nickname("닉네임")
                .tel("02123123")
                .email("test@example.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("신규 회원 등록할 때 핸드폰번호는 필수값이다.")
    @Test
    void createUserWithoutPhone() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password("123")
                .nickname("닉네임")
                .name("홍길동")
                .email("test@example.com")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("전화번호는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("신규 회원 등록할 때 이메일은 필수값이다.")
    @Test
    void createUserWithoutEmail() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password("123")
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .build();

        //when //then
        mockMvc.perform(post("/api/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());;
    }

    @DisplayName("회원 목록을 조회 한다.")
    @Test
    void getUsers() throws Exception {
        //given
        int page = 0;
        int size = 3;

        // when // then
        mockMvc.perform(get("/api/user/list")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", "createDateTime,desc")
                        .param("sort", "name,asc")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
