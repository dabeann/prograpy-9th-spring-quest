package prograpy.quest.controller;

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
public class UserController {

    private final UserService userService;

    // 유저 전체 조회 API
    @GetMapping("/user")
    public ApiResponse<UserListResponse> findAllUsers(
            @RequestParam(name = "size", defaultValue = "0") int size,
            @RequestParam(name = "page", defaultValue = "10") int page) {
        return userService.findAllUsers(size, page);
    }

    // 팀 변경 API
    @PutMapping("/team/{roomId}")
    public ApiResponse<Object> changeTeam(@PathVariable Integer roomId, @RequestBody UserIdRequest userId) {
        return userService.changeTeam(roomId, userId.getUserId());
    }
}
