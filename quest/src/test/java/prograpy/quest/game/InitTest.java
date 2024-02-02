package prograpy.quest.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Commit;
import prograpy.quest.model.User;
import prograpy.quest.model.User.UserStatus;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.request.InitRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.GameService;

@SpringBootTest
public class InitTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserRoomRepository userRoomRepository;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    @DisplayName("init 성공 테스트")
    void success() {
        int seed = 1;
        int quantity = 10;
        InitRequest initRequest = new InitRequest(seed, quantity);

        ApiResponse<Object> response = gameService.init(initRequest);

        verify(userRepository, times(quantity)).save(any(User.class));
        verify(userRoomRepository, times(1)).deleteAll();
        verify(roomRepository, times(1)).deleteAll();

        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
        assertNull(response.getResult());
    }

}
