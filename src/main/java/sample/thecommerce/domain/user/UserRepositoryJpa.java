package sample.thecommerce.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpa extends JpaRepository<User, Long> {


    User findByUsername(String username);
}
