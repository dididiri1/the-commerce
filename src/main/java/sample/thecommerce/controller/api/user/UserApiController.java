package sample.thecommerce.controller.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.thecommerce.dto.ApiResponse;
import sample.thecommerce.dto.user.request.UserCreateRequest;
import sample.thecommerce.dto.user.request.UserUpdateRequest;
import sample.thecommerce.dto.user.response.UserCreateResponse;
import sample.thecommerce.dto.user.response.UserResponse;
import sample.thecommerce.dto.user.response.UserUpdateResponse;
import sample.thecommerce.service.user.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;


    /**
     * @Method: createUser
     * @Description: 회원 등록
     */
    @PostMapping("/api/user/join")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserCreateResponse response = userService.createUser(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "회원 등록 성공", response), HttpStatus.CREATED);
    }

    /**
     * @Method: getUsers
     * @Description: 회원 목록 조회
     */
    @GetMapping("/api/user/list")
    public ResponseEntity<?> getUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getUsers(pageable);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 목록 조회 성공", users), HttpStatus.OK);
    }

    /**
     * @Method: updateUser
     * @Description: 회원 목록 조회
     */
    @PatchMapping("/api/user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequest request) {
        UserUpdateResponse response = userService.updateUser(userId, request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 수정 성공", response), HttpStatus.OK);
    }
}
