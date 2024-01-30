package prograpy.quest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import prograpy.quest.model.User;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.UserListResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    // 유저 전체 조회
    @Override
    public ApiResponse<UserListResponse> findAllUsers(int size, int page) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
            Page<User> users = userRepository.findAll(pageRequest);

            UserListResponse response = UserListResponse.builder()
                    .totalElements((int) users.getTotalElements())
                    .totalPages(users.getTotalPages())
                    .userList(users.getContent())
                    .build();

            return ApiResponse.successResponse(response);

        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }
}
