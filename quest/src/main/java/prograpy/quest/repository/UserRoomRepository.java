package prograpy.quest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import prograpy.quest.model.User;
import prograpy.quest.model.UserRoom;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {
    Optional<UserRoom> findUserRoomsByUserId(User user);
}
