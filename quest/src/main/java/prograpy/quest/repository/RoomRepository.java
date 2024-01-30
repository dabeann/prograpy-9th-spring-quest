package prograpy.quest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prograpy.quest.model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
