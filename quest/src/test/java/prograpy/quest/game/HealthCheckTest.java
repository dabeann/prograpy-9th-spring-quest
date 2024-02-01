package prograpy.quest.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import prograpy.quest.request.InitRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.GameService;

@SpringBootTest
public class HealthCheckTest {
    
    @Autowired
    GameService gameService;

    @Test
    @DisplayName("healthCheck 성공 테스트")
    void success() {
        ApiResponse<Object> response = gameService.healthCheck();
        assertEquals(200, response.getCode());
        assertEquals("API 요청이 성공했습니다.", response.getMessage());
        assertNull(response.getResult());
    }

    @Test
    @DisplayName("healthCheck 실패 테스트")
    void error() {
        GameService gameServiceError = new GameService() {
            @Override
            public ApiResponse<Object> healthCheck() {
                return ApiResponse.errorResponse();
            }
            @Override
            public ApiResponse<Object> init(InitRequest initRequest) {
                return null;
            }
            @Override
            public ApiResponse<Object> gameStart(Integer roomId, Integer userId) {
                return null;
            }
        };
        ApiResponse<Object> response = gameServiceError.healthCheck();

        assertEquals(500, response.getCode());
        assertEquals("에러가 발생했습니다.", response.getMessage());
        assertNull(response.getResult());
    }
}
