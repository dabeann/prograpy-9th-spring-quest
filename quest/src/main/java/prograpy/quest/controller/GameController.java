package prograpy.quest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prograpy.quest.request.InitRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.GameService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    // 헬스체크 API
    @GetMapping("/health")
    public ApiResponse<Object> healthCheck(){
        return gameService.healthCheck();
    }

    // 초기화 API
    @PostMapping("/init")
    public ApiResponse<Object> init(@RequestBody InitRequest initRequest) {
        return gameService.init(initRequest);
    }

    // 게임시작 API

}
