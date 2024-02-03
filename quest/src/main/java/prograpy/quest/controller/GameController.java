package prograpy.quest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prograpy.quest.request.InitRequest;
import prograpy.quest.request.UserIdRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.GameService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "game", description = "게임 API")
public class GameController {

    private final GameService gameService;

    @Operation(summary = "헬스 체크", description = "서버 상태 체크", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/health")
    public ApiResponse<Object> healthCheck() {
        return gameService.healthCheck();
    }

    @Operation(summary = "초기화", description = "모든 데이터 삭제 후 유저들 저장", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/init")
    public ApiResponse<Object> init(@RequestBody InitRequest initRequest) {
        return gameService.init(initRequest);
    }

    @Operation(summary = "게임시작", description = "호스트인 유저가 게임 시작", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PutMapping("/room/start/{roomId}")
    public ApiResponse<Object> gameStart(@Parameter(description = "방 고유 아이디") @PathVariable Integer roomId, @Parameter(description = "유저 고유 아이디") @RequestBody UserIdRequest userId) {
        return gameService.gameStart(roomId, userId.getUserId());
    }
}
