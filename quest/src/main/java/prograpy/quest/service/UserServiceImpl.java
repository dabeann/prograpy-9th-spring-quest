package prograpy.quest.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prograpy.quest.model.Room;
import prograpy.quest.model.Room.RoomStatus;
import prograpy.quest.model.Room.RoomType;
import prograpy.quest.model.User;
import prograpy.quest.model.UserRoom;
import prograpy.quest.model.UserRoom.Team;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.UserListResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

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

    // 팀 변경
    @Override
    @Transactional
    public ApiResponse<Object> changeTeam(Integer roomId, Integer userId) {
        try {
            Optional<Room> findRoom = roomRepository.findById(roomId);
            Optional<User> findUser = userRepository.findById(userId);

            if (findRoom.isEmpty() || findUser.isEmpty()) {
                // 존재하지 않는 id에 대한 요청 - 201 response
                return ApiResponse.failResponse();
            }

            Room room = findRoom.get();
            User user = findUser.get();

            // Room의 상태가 WAIT일때만 가능
            if (!room.getStatus().equals(RoomStatus.WAIT)) {
                return ApiResponse.failResponse();
            }

            // User가 Room에 참가한 상태에서만 가능
            Optional<UserRoom> findUserRoom = userRoomRepository.findUserRoomByRoomIdAndUserId(room, user);
            if (findUserRoom.isEmpty()) {
                return ApiResponse.failResponse();
            }
            UserRoom userRoom = findUserRoom.get();

            // 변경되려는 팀의 인원이 이미 해당 방 정원의 절반과 같다면 팀 변경X - 201 response
            // 바꾸려는 team
            Team changeTeam = Team.RED;
            if (userRoom.getTeam().equals(Team.RED)) {
                changeTeam = Team.BLUE;
            }
            Integer teamMemberNum = userRoomRepository.countUserRoomsByRoomIdAndTeam(room, changeTeam);
            if (room.getRoomType().equals(RoomType.SINGLE)) {
                // SINGLE인 경우 바꾸려는 팀 인원이 1이라면 변경X - 201 response
                if (teamMemberNum.equals(1)) {
                    return ApiResponse.failResponse();
                }
            } else {
                // DOUBLE인 경우 바꾸려는 팀 인원이 2라면 변경X - 201 response
                if (teamMemberNum.equals(2)) {
                    return ApiResponse.failResponse();
                }
            }

            // 팀 변경하기
            userRoomRepository.updateTeamByRoomIdAndUserId(room, user, changeTeam);

            return ApiResponse.successResponse(null);
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }
}
