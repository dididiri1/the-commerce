package sample.thecommerce.docs.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.thecommerce.controller.api.user.UserApiController;
import sample.thecommerce.docs.RestDocsSupport;
import sample.thecommerce.dto.user.request.UserCreateRequest;
import sample.thecommerce.dto.user.request.UserUpdateRequest;
import sample.thecommerce.dto.user.response.UserCreateResponse;
import sample.thecommerce.dto.user.response.UserResponse;
import sample.thecommerce.dto.user.response.UserUpdateResponse;
import sample.thecommerce.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserApiControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserApiController(userService);
    }


    @Test
    @DisplayName("신규 유저를 등록하는 API")
    void createUser() throws Exception {

        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test")
                .password("1234")
                .nickname("닉네임")
                .name("홍길동")
                .tel("02123123")
                .email("test@example.com")
                .build();

        given(userService.createUser(any(UserCreateRequest.class)))
                .willReturn(UserCreateResponse.builder()
                        .id(1L)
                        .username("test")
                        .name("홍길동")
                        .email("test@example.com")
                        .build());

        // expected
        this.mockMvc.perform(post("/api/user/join")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("user-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("사용자명"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("닉네임"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이름"),
                                fieldWithPath("tel").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이메일")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("사용자명"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름")
                        )
                ));
    }

    @DisplayName("회원 목록 조회 API")
    @Test
    void getUserList() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String createDateTimeOrder = "createDateTime,desc";
        String nameOrder = "name,asc";

        List<UserResponse> content = List.of(
                UserResponse.builder()
                        .userId(1L)
                        .username("testUser")
                        .nickname("라이언")
                        .name("홍길동")
                        .tel("02123123")
                        .email("test@gmail.com")
                        .createDateTime(LocalDateTime.of(2018, 12, 15, 10, 0, 0))
                        .build()
        );
        Page<UserResponse> result = new PageImpl<>(content, pageable, 2);
        given(userService.getUsers(any(Pageable.class)))
                .willReturn(result);

        // when // then
        this.mockMvc.perform(get("/api/user/list?page=0&size=10&sort=createDateTime,desc&sort=name,asc")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("게시물 사이즈"),
                                parameterWithName("sort").description("정렬 순서")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.content[].userId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("data.content[].username").type(JsonFieldType.STRING).description("사용자명"),
                                fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.content[].tel").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("data.content[].email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.content[].createDateTime").type(JsonFieldType.STRING).description("등록일"),

                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안됬는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 됬는지 여부"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("데이터 개수"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("몇번째 데이터인지"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 정보를 포함하는지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 정보를 안포함하는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안됬는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 됬는지 여부"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("요청 페이지에서 조회된 데이터 개수"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("테이블 총 데이터 개수"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지인지 여부"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 조회할 데이터 개수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("회원 정보를 수정 API")
    void updateUser() throws Exception {
        // given
        long userId = 1L;

        UserUpdateRequest request = UserUpdateRequest.builder()
                .email("test2@gmail.com")
                .name("김구라")
                .nickname("춘식이")
                .build();

        given(userService.updateUser(any(Long.class), any(UserUpdateRequest.class)))
                .willReturn(UserUpdateResponse.builder()
                        .userId(1L)
                        .username("testUser")
                        .email("test2@gmail.com")
                        .name("김구라")
                        .nickname("춘식이")
                        .build());

        // expected
        this.mockMvc.perform(patch("/api/user/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("회원 ID")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("사용자명"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임")
                        )
                ));
    }

}
