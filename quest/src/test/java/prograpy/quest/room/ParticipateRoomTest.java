package prograpy.quest.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
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
import prograpy.quest.model.User.UserStatus;
import prograpy.quest.model.UserRoom;
import prograpy.quest.model.UserRoom.Team;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.RoomService;
import prograpy.quest.service.RoomServiceImpl;

@SpringBootTest
public class ParticipateRoomTest {

    private RoomService roomService;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private UserRoomRepository userRoomRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roomRepository = mock(RoomRepository.class);
        userRoomRepository = mock(UserRoomRepository.class);
        roomService = new RoomServiceImpl(userRepository, userRoomRepository, roomRepository);
    }

    @Test
    @DisplayName("방 참가 성공 테스트")
    void participateRoomSuccess() {
        User fakeUser = User.builder().id(1).status(UserStatus.ACTIVE).build();
        User host = User.builder().id(2).status(UserStatus.ACTIVE).build();
        Room fakeRoom = Room.builder().id(1).host(host).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.countUserRoomsByRoomId(fakeRoom)).thenReturn(0);
        when(userRoomRepository.countUserRoomsByUserId(fakeUser)).thenReturn(0);
        when(userRoomRepository.countUserRoomsByRoomIdAndTeam(fakeRoom, Team.RED)).thenReturn(0);
        when(userRoomRepository.countUserRoomsByRoomIdAndTeam(fakeRoom, Team.BLUE)).thenReturn(0);

        ApiResponse<Object> response = roomService.participateRoom(1, 1);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 참가 실패 테스트 - 대기 상태가 아닌 방")
    void participateRoomFailureNotWaitStatus() {
        User fakeUser = User.builder().id(1).status(UserStatus.ACTIVE).build();
        Room fakeRoom = Room.builder().id(1).status(RoomStatus.PROGRESS).room_type(RoomType.SINGLE).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));

        ApiResponse<Object> response = roomService.participateRoom(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 참가 실패 테스트 - 비활성 상태인 유저")
    void participateRoomFailureInactiveUser() {
        User fakeUser = User.builder().id(1).status(UserStatus.NON_ACTIVE).build();
        Room fakeRoom = Room.builder().id(1).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));

        ApiResponse<Object> response = roomService.participateRoom(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 참가 실패 테스트 - 이미 참여 중인 유저")
    void participateRoomFailureAlreadyJoined() {
        User fakeUser = User.builder().id(1).status(UserStatus.ACTIVE).build();
        Room fakeRoom = Room.builder().id(1).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();
        UserRoom fakeUserRoom = UserRoom.builder().userId(fakeUser).roomId(fakeRoom).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.countUserRoomsByUserId(fakeUser)).thenReturn(1);

        ApiResponse<Object> response = roomService.participateRoom(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 참가 실패 테스트 - 방 정원 초과")
    void participateRoomFailureExceedCapacity() {
        User fakeUser = User.builder().id(1).status(UserStatus.ACTIVE).build();
        Room fakeRoom = Room.builder().id(1).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.countUserRoomsByRoomId(fakeRoom)).thenReturn(2);

        ApiResponse<Object> response = roomService.participateRoom(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
    }
}
