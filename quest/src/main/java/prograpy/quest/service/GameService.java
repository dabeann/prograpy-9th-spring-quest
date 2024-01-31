package prograpy.quest.service;

import prograpy.quest.request.InitRequest;
import prograpy.quest.response.ApiResponse;

public interface GameService {

    ApiResponse<Object> healthCheck();

    ApiResponse<Object> init(InitRequest initRequest);

    ApiResponse<Object> gameStart(Integer roomId, Integer userId);
}
