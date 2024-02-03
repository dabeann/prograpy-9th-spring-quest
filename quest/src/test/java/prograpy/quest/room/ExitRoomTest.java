package prograpy.quest.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
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
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.RoomService;
import prograpy.quest.service.RoomServiceImpl;

@SpringBootTest
public class ExitRoomTest {

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
    @DisplayName("방 나가기 성공 테스트 - 일반 참가자")
    void exitRoomSuccess() {
        User fakeUser = User.builder().id(1).build();
        Room fakeRoom = Room.builder().id(1).host(fakeUser).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();
        UserRoom fakeUserRoom = UserRoom.builder().userId(fakeUser).roomId(fakeRoom).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(fakeRoom, fakeUser)).thenReturn(Optional.of(fakeUserRoom));

        ApiResponse<Object> response = roomService.exitRoom(1, 1);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 나가기 성공 테스트 - 호스트")
    void exitRoomSuccessHost() {
        User fakeUser = User.builder().id(1).build();
        Room fakeRoom = Room.builder().id(1).host(fakeUser).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();
        UserRoom fakeUserRoom = UserRoom.builder().userId(fakeUser).roomId(fakeRoom).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(fakeRoom, fakeUser)).thenReturn(Optional.of(fakeUserRoom));

        ApiResponse<Object> response = roomService.exitRoom(1, 1);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 나가기 실패 테스트 - 참가 상태가 아닌 유저")
    void exitRoomFailureNotJoined() {
        User fakeUser = User.builder().id(1).build();
        Room fakeRoom = Room.builder().id(1).host(fakeUser).status(RoomStatus.WAIT).room_type(RoomType.SINGLE).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(fakeRoom, fakeUser)).thenReturn(Optional.empty());

        ApiResponse<Object> response = roomService.exitRoom(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
    }

    @Test
    @DisplayName("방 나가기 실패 테스트 - 이미 시작된 방")
    void exitRoomFailureAlreadyInProgress() {
        User fakeUser = User.builder().id(1).build();
        Room fakeRoom = Room.builder().id(1).host(fakeUser).status(RoomStatus.PROGRESS).room_type(RoomType.SINGLE).build();
        UserRoom fakeUserRoom = UserRoom.builder().userId(fakeUser).roomId(fakeRoom).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(fakeUser));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        when(userRoomRepository.findUserRoomByRoomIdAndUserId(fakeRoom, fakeUser)).thenReturn(Optional.of(fakeUserRoom));

        ApiResponse<Object> response = roomService.exitRoom(1, 1);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
    }
}
