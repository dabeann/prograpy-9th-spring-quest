package prograpy.quest.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import prograpy.quest.model.Room;
import prograpy.quest.model.User;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.RoomListResponse;
import prograpy.quest.response.RoomListResponse.PageRoom;
import prograpy.quest.service.RoomService;
import prograpy.quest.service.RoomServiceImpl;

@SpringBootTest
public class FindAllRoomsTest {

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
    @DisplayName("findAllRooms 성공 테스트")
    void success() {
        User user1 = User.builder().id(1).name("hi").build();
        User user2 = User.builder().id(2).name("hi2").build();
        Room room1 = Room.builder().id(1).host(user1).title("Room1").build();
        Room room2 = Room.builder().id(2).host(user2).id(2).title("Room2").build();

        PageImpl<Room> fakePage = new PageImpl<>(List.of(room1, room2));
        when(roomRepository.findAll(any(PageRequest.class))).thenReturn(fakePage);

        ApiResponse<RoomListResponse> response = roomService.findAllRooms(10, 0);

        verify(roomRepository, times(1)).findAll(any(PageRequest.class));

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
        assertNotNull(response.getResult());

        List<PageRoom> expectedRooms = new ArrayList<>();
        for (Room room : fakePage.getContent()) {
            PageRoom pageRoom = PageRoom.fromEntity(room);
            expectedRooms.add(pageRoom);
        }

        assertEquals(expectedRooms.size(), response.getResult().getRoomList().size());
        for (int i = 0; i < expectedRooms.size(); i++) {
            assertEquals(expectedRooms.get(i).getTitle(), response.getResult().getRoomList().get(i).getTitle());
            System.out.println("expectedRooms = " + expectedRooms.get(i).getTitle());
            System.out.println("response = " + response.getResult().getRoomList().get(i).getTitle());
        }
    }
}
