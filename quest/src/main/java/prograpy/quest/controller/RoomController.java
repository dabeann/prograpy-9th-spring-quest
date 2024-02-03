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
@Tag(name = "room", description = "방 API")
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "방 생성", description = "request 정보로 방 생성", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/room")
    public ApiResponse<Object> createRoom(@RequestBody RoomCreateRequest request) {
        return roomService.createRoom(request);
    }

    @Operation(summary = "방 전체 조회", description = "request 정보로 페이징 처리한 방 리스트 반환", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/room")
    public ApiResponse<RoomListResponse> findAllRooms(@RequestParam(name = "size", defaultValue = "0") int size,
                                                      @RequestParam(name = "page", defaultValue = "10") int page) {
        return roomService.findAllRooms(size, page);
    }

    @Operation(summary = "방 상세 조회", description = "방 고유 아이디에 대한 방 정보 반환", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/room/{roomId}")
    public ApiResponse<RoomDto> findRoomDetails(@Parameter(description = "방 고유 아이디") @PathVariable Integer roomId) {
        return roomService.findRoomDetails(roomId);
    }

    @Operation(summary = "방 참가", description = "유저가 방에 참가", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/room/attention/{roomId}")
    public ApiResponse<Object> participateRoom(@Parameter(description = "방 고유 아이디") @PathVariable Integer roomId, @Parameter(description = "유저 고유 아이디") @RequestBody UserIdRequest userId) {
        return roomService.participateRoom(roomId, userId.getUserId());
    }

    @Operation(summary = "방 나가기", description = "유저가 방에서 나감", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/room/out/{roomId}")
    public ApiResponse<Object> exitRoom(@Parameter(description = "방 고유 아이디") @PathVariable Integer roomId, @Parameter(description = "유저 고유 아이디") @RequestBody UserIdRequest userId) {
        return roomService.exitRoom(roomId, userId.getUserId());
    }
}
