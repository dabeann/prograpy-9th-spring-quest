package prograpy.quest.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import prograpy.quest.model.Room;
import prograpy.quest.model.Room.RoomStatus;
import prograpy.quest.model.Room.RoomType;
import prograpy.quest.model.User;
import prograpy.quest.model.UserRoom;
import prograpy.quest.model.UserRoom.Team;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.UserService;
import prograpy.quest.service.UserServiceImpl;

@SpringBootTest
public class ChangeTeamTest {
    private UserService userService;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private UserRoomRepository userRoomRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roomRepository = mock(RoomRepository.class);
        userRoomRepository = mock(UserRoomRepository.class);
        userService = new UserServiceImpl(userRepository, roomRepository, userRoomRepository);
    }

    @Test
    public void changeTeamSuccess() {
        User user = User.builder().id(1).name("user1").build();
        Room room = Room.builder().id(1).title("Room 1").room_type(RoomType.SINGLE).status(RoomStatus.WAIT).build();
        UserRoom userRoom = UserRoom.builder().id(1).userId(user).roomId(room).team(Team.RED).build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(room, user)).thenReturn(Optional.of(userRoom));
        when(userRoomRepository.countUserRoomsByRoomIdAndTeam(room, Team.BLUE)).thenReturn(0);

        ApiResponse<Object> response = userService.changeTeam(1, 1);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());

        verify(userRoomRepository, times(1)).updateTeamByRoomIdAndUserId(room, user, Team.BLUE);
    }

    @Test
    @DisplayName("유저가 방에 속해있지 않을 경우")
    public void changeTeamFailUserNotInRoom() {
        User user = User.builder().id(1).name("user1").build();
        Room room = Room.builder().id(1).title("Room 1").room_type(RoomType.SINGLE).status(RoomStatus.WAIT).build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(room, user)).thenReturn(Optional.empty());

        ApiResponse<Object> response = userService.changeTeam(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());

        verify(userRoomRepository, never()).updateTeamByRoomIdAndUserId(any(Room.class), any(User.class), any(Team.class));
    }

    @Test
    @DisplayName("변경하려는 팀이 이미 해당 방 정원의 절반과 같은 경우")
    public void changeTeamFailTeamAlreadyHalfFull() {
        User user = User.builder().id(1).name("user1").build();
        Room room = Room.builder().id(1).title("Room 1").room_type(RoomType.SINGLE).status(RoomStatus.WAIT).build();
        UserRoom userRoom = UserRoom.builder().id(1).userId(user).roomId(room).team(Team.RED).build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(room, user)).thenReturn(Optional.of(userRoom));
        when(userRoomRepository.countUserRoomsByRoomIdAndTeam(room, Team.BLUE)).thenReturn(1);

        ApiResponse<Object> response = userService.changeTeam(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());

        verify(userRoomRepository, never()).updateTeamByRoomIdAndUserId(any(Room.class), any(User.class), any(Team.class));
    }
}
