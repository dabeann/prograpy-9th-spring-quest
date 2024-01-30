package prograpy.quest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prograpy.quest.request.RoomCreateRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.service.RoomService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    // 방 생성
    @PostMapping("/room")
    public ApiResponse<Object> createRoom(@RequestBody RoomCreateRequest request) {
        return roomService.createRoom(request);
    }

    // 방 나가기

    // 방 참가

    // 방 상세 조회

    // 방 전체 조회
}
