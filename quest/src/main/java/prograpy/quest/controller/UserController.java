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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prograpy.quest.request.UserIdRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.UserListResponse;
import prograpy.quest.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "user", description = "유저 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 전체 조회", description = "모든 유저들 정보 반환", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/user")
    public ApiResponse<UserListResponse> findAllUsers(
            @RequestParam(name = "size", defaultValue = "0") int size,
            @RequestParam(name = "page", defaultValue = "10") int page) {
        return userService.findAllUsers(size, page);
    }

    @Operation(summary = "팀 변경", description = "다른 팀으로 변경", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PutMapping("/team/{roomId}")
    public ApiResponse<Object> changeTeam(@Parameter(description = "방 고유 아이디") @PathVariable Integer roomId, @Parameter(description = "유저 고유 아이디") @RequestBody UserIdRequest userId) {
        return userService.changeTeam(roomId, userId.getUserId());
    }
}
