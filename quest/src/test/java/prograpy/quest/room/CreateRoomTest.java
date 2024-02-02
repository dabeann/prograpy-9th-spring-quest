package prograpy.quest.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import prograpy.quest.model.Room;
import prograpy.quest.model.User;
import prograpy.quest.model.User.UserStatus;
import prograpy.quest.model.UserRoom;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.request.RoomCreateRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.RoomService;
import prograpy.quest.service.RoomServiceImpl;

@SpringBootTest
public class CreateRoomTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserRoomRepository userRoomRepository;

    @MockBean
    private RoomRepository roomRepository;

    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomServiceImpl(userRepository, userRoomRepository, roomRepository);
    }

    @Test
    @DisplayName("활성 사용자, 참여 중인 방 없음")
    void createRoom_Success() {
        User hostUser = User.builder()
                .id(1)
                .name("hostUser")
                .email("host@example.com")
                .status(UserStatus.ACTIVE)
                .build();

        RoomCreateRequest request = mock(RoomCreateRequest.class);
        when(request.getUserId()).thenReturn(1);
        when(request.getRoomType()).thenReturn("DOUBLE");
        when(request.getTitle()).thenReturn("Room Title");

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(hostUser));
        when(userRoomRepository.findUserRoomsByUserId(hostUser)).thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse<Object> response = roomService.createRoom(request);

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(hostUser);
        verify(roomRepository, times(1)).save(any(Room.class));

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
        assertNull(response.getResult());
    }

    @Test
    @DisplayName("비활성 사용자")
    void createRoom_InactiveUser() {
        User inactiveUser = User.builder()
                .id(1)
                .name("inactiveUser")
                .email("inactive@example.com")
                .status(UserStatus.NON_ACTIVE)
                .build();

        RoomCreateRequest request = mock(RoomCreateRequest.class);
        when(request.getUserId()).thenReturn(1);
        when(request.getRoomType()).thenReturn("DOUBLE");
        when(request.getTitle()).thenReturn("Room Title");

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(inactiveUser));

        ApiResponse<Object> response = roomService.createRoom(request);

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(inactiveUser);
        verify(roomRepository, never()).save(any(Room.class));

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
        assertNull(response.getResult());
    }

    @Test
    @DisplayName("참여 중인 방이 있는 사용자")
    void createRoom_UserAlreadyParticipating() {
        User userWithRoom = User.builder()
                .id(1)
                .name("userWithRoom")
                .email("userWithRoom@example.com")
                .status(UserStatus.ACTIVE)
                .build();

        RoomCreateRequest request = mock(RoomCreateRequest.class);
        when(request.getUserId()).thenReturn(1);
        when(request.getRoomType()).thenReturn("DOUBLE");
        when(request.getTitle()).thenReturn("Room Title");

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(userWithRoom));
        when(userRoomRepository.findUserRoomsByUserId(userWithRoom)).thenReturn(Optional.of(mock(UserRoom.class)));

        ApiResponse<Object> response = roomService.createRoom(request);

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(userWithRoom);
        verify(roomRepository, never()).save(any(Room.class));

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
        assertNull(response.getResult());
    }
}
