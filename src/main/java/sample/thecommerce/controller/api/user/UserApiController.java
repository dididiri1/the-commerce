package sample.thecommerce.controller.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.thecommerce.dto.ApiResponse;
import sample.thecommerce.dto.user.request.UserCreateRequest;
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
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest request, BindingResult bindingResult) {
        userService.createUser(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "회원 등록 성공", null), HttpStatus.CREATED);
    }
}
