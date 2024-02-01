package prograpy.quest.service;

import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.UserListResponse;

public interface UserService {

    ApiResponse<UserListResponse> findAllUsers(int size, int page);

    ApiResponse<Object> changeTeam(Integer roomId, Integer userId);
}
