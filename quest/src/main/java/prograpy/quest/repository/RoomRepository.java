package prograpy.quest.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import prograpy.quest.model.Room;
import prograpy.quest.model.Room.RoomStatus;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Modifying
    @Query("UPDATE Room r SET r.status = :status, r.updatedAt = :time WHERE r.updatedAt <= :ago AND r.status = :waitStatus")
    void updateRoomStatusAfterTime(RoomStatus status, LocalDateTime time, LocalDateTime ago, RoomStatus waitStatus);

    Optional<Room> findRoomByStatus(RoomStatus status);
}
