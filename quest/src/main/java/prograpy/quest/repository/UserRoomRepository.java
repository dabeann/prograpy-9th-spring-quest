package prograpy.quest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import prograpy.quest.model.Room;
import prograpy.quest.model.User;
import prograpy.quest.model.UserRoom;
import prograpy.quest.model.UserRoom.Team;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {
    Optional<UserRoom> findUserRoomsByUserId(User user);

    Integer countUserRoomsByRoomId(Room room);

    Integer countUserRoomsByUserId(User user);

    Integer countUserRoomsByRoomIdAndTeam(Room room, Team team);

    Optional<UserRoom> findUserRoomByRoomIdAndUserId(Room room, User user);

    void deleteUserRoomsByRoomId(Room room);

    void deleteUserRoomByRoomIdAndUserId(Room room, User user);

    @Modifying
    @Query("UPDATE UserRoom ur SET ur.team = :team WHERE ur.roomId = :roomId AND ur.userId = :userId")
    void updateTeamByRoomIdAndUserId(Room roomId, User userId, Team team);
}
