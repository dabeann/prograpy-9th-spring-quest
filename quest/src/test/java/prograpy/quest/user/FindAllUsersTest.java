package prograpy.quest.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import prograpy.quest.model.User;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.UserListResponse;
import prograpy.quest.service.UserService;
import prograpy.quest.service.UserServiceImpl;

@SpringBootTest
public class FindAllUsersTest {

    private UserService userService;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private UserRoomRepository userRoomRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, roomRepository, userRoomRepository);
    }

    @Test
    @DisplayName("findAllUsers 성공 테스트")
    void success() {
        User user1 = User.builder().name("hi").build();
        User user2 = User.builder().name("hi2").build();

        PageImpl<User> fakePage = new PageImpl<>(List.of(user1, user2));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(fakePage);

        ApiResponse<UserListResponse> response = userService.findAllUsers(10, 0);

        verify(userRepository, times(1)).findAll(any(PageRequest.class));

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
        assertNotNull(response.getResult());

        assertEquals(fakePage.getContent(), response.getResult().getUserList());
        System.out.println("fakePage.getContent() = " + fakePage.getContent());
        System.out.println("response.getResult.getUserList() = " + response.getResult().getUserList());
    }
}
