package prograpy.quest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prograpy.quest.model.UserRoom;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {
}
