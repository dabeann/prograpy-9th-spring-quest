package prograpy.quest.service;

import prograpy.quest.request.RoomCreateRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.RoomDto;
import prograpy.quest.response.RoomListResponse;

public interface RoomService {

    ApiResponse<Object> createRoom(RoomCreateRequest request);

    ApiResponse<RoomListResponse> findAllRooms(int size, int page);

    ApiResponse<RoomDto> findRoomDetails(Integer roomId);

    ApiResponse<Object> participateRoom(Integer roomId, Integer userId);

    ApiResponse<Object> exitRoom(Integer roomId, Integer userId);
}
