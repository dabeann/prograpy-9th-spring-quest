package prograpy.quest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prograpy.quest.request.RoomCreateRequest;
import prograpy.quest.request.UserIdRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.RoomDto;
import prograpy.quest.response.RoomListResponse;
import prograpy.quest.service.RoomService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    // 방 생성 API
    @PostMapping("/room")
    public ApiResponse<Object> createRoom(@RequestBody RoomCreateRequest request) {
        return roomService.createRoom(request);
    }

    // 방 전체 조회 API
    @GetMapping("/room")
    public ApiResponse<RoomListResponse> findAllRooms(@RequestParam(name = "size", defaultValue = "0") int size,
                                                      @RequestParam(name = "page", defaultValue = "10") int page) {
        return roomService.findAllRooms(size, page);
    }

    // 방 상세 조회 API
    @GetMapping("/room/{roomId}")
    public ApiResponse<RoomDto> findRoomDetails(@PathVariable Integer roomId) {
        return roomService.findRoomDetails(roomId);
    }

    // 방 참가 API
    @PostMapping("/room/attention/{roomId}")
    public ApiResponse<Object> participateRoom(@PathVariable Integer roomId, @RequestBody UserIdRequest userId) {
        return roomService.participateRoom(roomId, userId.getUserId());
    }

    // 방 나가기 API
    @PostMapping("/room/out/{roomId}")
    public ApiResponse<Object> exitRoom(@PathVariable Integer roomId, @RequestBody UserIdRequest userId) {
        return roomService.exitRoom(roomId, userId.getUserId());
    }
}
