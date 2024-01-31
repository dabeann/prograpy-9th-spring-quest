package prograpy.quest.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import prograpy.quest.response.RoomDto;
import prograpy.quest.response.RoomListResponse;
import prograpy.quest.response.RoomListResponse.PageRoom;

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

    // 방 전체 조회 API
    @Override
    public ApiResponse<RoomListResponse> findAllRooms(int size, int page) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
            Page<Room> rooms = roomRepository.findAll(pageRequest);

            List<PageRoom> pageRooms = new ArrayList<>();
            for (Room room : rooms) {
                PageRoom pageRoom = PageRoom.fromEntity(room);
                pageRooms.add(pageRoom);
            }

            RoomListResponse roomListResponse = RoomListResponse.builder()
                    .totalElements((int) rooms.getTotalElements())
                    .totalPages(rooms.getTotalPages())
                    .roomList(pageRooms)
                    .build();

            return ApiResponse.successResponse(roomListResponse);
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }

    // 방 상세 조회 API
    @Override
    public ApiResponse<RoomDto> findRoomDetails(Integer roomId) {
        try {
            Optional<Room> findRoom = roomRepository.findById(roomId);
            if (findRoom.isPresent()) {
                Room room = findRoom.get();
                RoomDto roomDto = RoomDto.fromEntity(room);
                return ApiResponse.successResponse(roomDto);
            } else {
                // 존재하지 않는 id에 대한 요청 - 201 response
                return ApiResponse.failResponse();
            }
        } catch (Exception e) {
            return ApiResponse.failResponse();
        }
    }

}