package prograpy.quest.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import prograpy.quest.model.Room;
import prograpy.quest.model.User;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.RoomDto;
import prograpy.quest.service.RoomService;
import prograpy.quest.service.RoomServiceImpl;

@SpringBootTest
public class FindRoomDetailsTest {

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
    @DisplayName("findRoomDetails 성공 테스트")
    void findRoomDetailsSuccess() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User fakeUser = User.builder().id(1).name("fakeUser").build();
        Room fakeRoom = Room.builder()
                .id(1)
                .host(fakeUser)
                .title("Test Room")
                .created_at(now)
                .updated_at(now)
                .build();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(fakeRoom));
        ApiResponse<RoomDto> response = roomService.findRoomDetails(1);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());

        RoomDto roomDto = response.getResult();
        assertNotNull(roomDto);
        assertEquals("Test Room", roomDto.getTitle());
    }

    @Test
    @DisplayName("findRoomDetails 실패 테스트")
    void findRoomDetailsFail() {
        when(roomRepository.findById(anyInt())).thenReturn(Optional.empty());
        ApiResponse<RoomDto> response = roomService.findRoomDetails(999);

        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals("불가능한 요청입니다.", response.getMessage());
        assertNull(response.getResult());
    }
}
