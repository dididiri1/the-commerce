package sample.thecommerce.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.thecommerce.domain.user.User;
import sample.thecommerce.domain.user.UserQueryRepository;
import sample.thecommerce.domain.user.UserRepositoryJpa;
import sample.thecommerce.dto.user.request.UserCreateRequest;
import sample.thecommerce.dto.user.request.UserUpdateRequest;
import sample.thecommerce.dto.user.response.UserCreateResponse;
import sample.thecommerce.dto.user.response.UserResponse;
import sample.thecommerce.dto.user.response.UserUpdateResponse;
import sample.thecommerce.handler.ex.validationException;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepositoryJpa userRepositoryJpa;

    private final UserQueryRepository userQueryRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        validateDuplicateUsername(request.getUsername());
        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        User userEntity = userRepositoryJpa.save(user);

        return UserCreateResponse.of(userEntity);
    }

    private void validateDuplicateUsername(String username) {
        User userEntity = userRepositoryJpa.findByUsername(username);
        if (userEntity != null) {
            throw new validationException("이미 사용 중인 유저명입니다.");
        }
    }

    public Page<UserResponse> getUsers(Pageable pageable) {
        return userQueryRepository.searchPageUsers(pageable);
    }

    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdateRequest request) {
        User userEntity = validateUserId(userId);
        if (request.getEmail() != null && !request.getEmail().equals("")) {
            userEntity.setEmail(request.getEmail());
        }
        if (request.getName() != null && !request.getName().equals("")) {
            userEntity.setName(request.getName());
        }
        if (request.getNickname() != null && !request.getNickname().equals("")) {
            userEntity.setNickname(request.getNickname());
        }

        return UserUpdateResponse.of(userEntity);
    }

    private User validateUserId(Long userId) {
        User findUser = userRepositoryJpa.findById(userId).orElseThrow(() -> {
            throw new validationException("해당 유저를 찾을수 없습니다.");
        });

        return findUser;
    }
}
