package prograpy.quest.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.GameService;
import prograpy.quest.service.GameServiceImpl;

@SpringBootTest
public class GameStartTest {

    private GameService gameService;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private UserRoomRepository userRoomRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roomRepository = mock(RoomRepository.class);
        userRoomRepository = mock(UserRoomRepository.class);
        gameService = new GameServiceImpl(userRepository, roomRepository, userRoomRepository, objectMapper);
    }

    @Test
    public void gameStartSuccess() {
        User hostUser = User.builder().id(1).name("hostUser").status(UserStatus.ACTIVE).build();
        Room room = Room.builder().id(1).title("Game Room").host(hostUser).room_type(RoomType.SINGLE).status(RoomStatus.WAIT)
                .created_at(LocalDateTime.now()).build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(hostUser));
        when(userRoomRepository.countUserRoomsByRoomId(room)).thenReturn(2);

        ApiResponse<Object> response = gameService.gameStart(1, 1);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    @DisplayName("꽉 찬 상태가 아님")
    public void gameStartFail() {
        User hostUser = User.builder().id(1).name("hostUser").status(UserStatus.ACTIVE).build();
        Room room = Room.builder().id(1).title("Game Room").host(hostUser).room_type(RoomType.SINGLE).status(RoomStatus.WAIT)
                .created_at(LocalDateTime.now()).build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(hostUser));
        when(userRoomRepository.countUserRoomsByRoomId(room)).thenReturn(1);

        ApiResponse<Object> response = gameService.gameStart(2, 2);

        assertNotNull(response);
        assertEquals(201, response.getCode());

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    @DisplayName("WAIT 상태가 아님")
    public void gameStartFailNotWait() {
        User hostUser = User.builder().id(1).name("hostUser").status(UserStatus.ACTIVE).build();
        Room room = Room.builder().id(1).title("Game Room").host(hostUser).room_type(RoomType.SINGLE).status(RoomStatus.PROGRESS)
                .created_at(LocalDateTime.now()).build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(hostUser));
        when(userRoomRepository.countUserRoomsByRoomId(room)).thenReturn(2);

        ApiResponse<Object> response = gameService.gameStart(2, 2);

        assertNotNull(response);
        assertEquals(201, response.getCode());

        verify(roomRepository, never()).save(any(Room.class));
    }
}
