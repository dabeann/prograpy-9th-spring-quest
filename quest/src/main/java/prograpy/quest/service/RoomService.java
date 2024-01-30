package prograpy.quest.service;

import prograpy.quest.request.RoomCreateRequest;
import prograpy.quest.response.ApiResponse;

public interface RoomService {

    ApiResponse<Object> createRoom(RoomCreateRequest request);
}
