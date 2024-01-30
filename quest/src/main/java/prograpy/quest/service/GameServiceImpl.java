package prograpy.quest.service;

import org.springframework.stereotype.Service;
import prograpy.quest.response.ApiResponse;

@Service
public class GameServiceImpl implements GameService{
    @Override
    public ApiResponse<Object> healthCheck() {
        try {
            return ApiResponse.successResponse(null);
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }
}
