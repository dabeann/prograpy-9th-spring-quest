package prograpy.quest.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prograpy.quest.model.Room;
import prograpy.quest.model.Room.RoomStatus;
import prograpy.quest.model.Room.RoomType;
import prograpy.quest.model.User;
import prograpy.quest.model.User.UserStatus;
import prograpy.quest.model.UserRoom;
import prograpy.quest.model.UserRoom.Team;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.request.RoomCreateRequest;
import prograpy.quest.response.ApiResponse;
@Service
@RequiredArgsConstructor

public class RoomServiceImpl implements RoomService{

    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;
    private final RoomRepository roomRepository;

    // 방 생성 API
    @Override
    public ApiResponse<Object> createRoom(RoomCreateRequest request) {

        try {
            Optional<User> hostUser = userRepository.findById(request.getUserId());

            if (hostUser.isEmpty()){
                // 잘못된 user id
                return ApiResponse.failResponse();
            }

            Optional<UserRoom> participatingRoom = userRoomRepository.findUserRoomsByUserId(hostUser.get());

            if (hostUser.get().getStatus().equals(UserStatus.ACTIVE) && participatingRoom.isEmpty()) {
                // 방 생성 가능
                LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

                Room room = Room.builder()
                        .title(request.getTitle())
                        .host(hostUser.get())
                        .room_type(RoomType.valueOf(request.getRoomType()))
                        .status(RoomStatus.WAIT)
                        .created_at(now)
                        .updated_at(now)
                        .build();
                roomRepository.save(room);

                /**
                 * host가 방 나가면 방 사라짐
                 * -> host도 게임에 참가
                 * -> 양쪽 팀에 모두 자리가 있으므로 RED에 배정
                 */
                UserRoom userRoom = UserRoom.builder()
                        .roomId(room)
                        .userId(hostUser.get())
                        .team(Team.RED)
                        .build();
                userRoomRepository.save(userRoom);

                return ApiResponse.successResponse(null);
            } else {
                // 방 생성 불가능
                return ApiResponse.failResponse();
            }

        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }
}
