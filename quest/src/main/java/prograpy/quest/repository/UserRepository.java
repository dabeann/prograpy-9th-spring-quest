package prograpy.quest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import prograpy.quest.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer userId);
}
