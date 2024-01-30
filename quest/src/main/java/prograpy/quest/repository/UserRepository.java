package prograpy.quest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prograpy.quest.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
