package sample.thecommerce.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.thecommerce.domain.user.User;
import sample.thecommerce.domain.user.UserRepository;
import sample.thecommerce.dto.user.request.UserCreateRequest;
import sample.thecommerce.handler.ex.validationException;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void createUser(UserCreateRequest request) {
        validateDuplicateUsername(request.getUsername());
        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    private void validateDuplicateUsername(String username) {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            throw new validationException("이미 사용 중인 유저명입니다.");
        }
    }
}
