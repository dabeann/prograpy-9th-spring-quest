package prograpy.quest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.UserListResponse;
import prograpy.quest.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 유저 전체 조회
    @GetMapping("/user")
    public ApiResponse<UserListResponse> findAllUsers(
            @RequestParam(name = "size", defaultValue = "0") int size,
            @RequestParam(name = "page", defaultValue = "10") int page) {
        return userService.findAllUsers(size, page);
    }

    // 팀 변경
}
