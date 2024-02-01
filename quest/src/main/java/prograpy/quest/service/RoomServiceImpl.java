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
import org.springframework.transaction.annotation.Transactional;
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

    // 방 생성
    @Override
    public ApiResponse<Object> createRoom(RoomCreateRequest request) {
        try {
            Optional<User> hostUser = userRepository.findById(request.getUserId());

            if (hostUser.isEmpty()){
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

    // 방 전체 조회
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

    // 방 상세 조회
    @Override
    public ApiResponse<RoomDto> findRoomDetails(Integer roomId) {
        try {
            Optional<Room> findRoom = roomRepository.findById(roomId);
            if (findRoom.isPresent()) {
                Room room = findRoom.get();
                RoomDto roomDto = RoomDto.fromEntity(room);
                return ApiResponse.successResponse(roomDto);
            } else {
                return ApiResponse.failResponse();
            }
        } catch (Exception e) {
            return ApiResponse.failResponse();
        }
    }

    // 방 참가
    @Override
    public ApiResponse<Object> participateRoom(Integer roomId, Integer userId) {
        try {
            Optional<Room> findRoom = roomRepository.findById(roomId);
            Optional<User> findUser = userRepository.findById(userId);

            if (findRoom.isEmpty() || findUser.isEmpty()) {
                return ApiResponse.failResponse();
            }
            Room room = findRoom.get();
            User user = findUser.get();

            if (!room.getStatus().equals(RoomStatus.WAIT) || !user.getStatus().equals(UserStatus.ACTIVE)) {
                return ApiResponse.failResponse();
            }

            // Room의 정원이 가득 찬 경우
            Integer countByRoom = userRoomRepository.countUserRoomsByRoomId(room);
            RoomType roomType = room.getRoomType();
            if (roomType.equals(RoomType.DOUBLE) && countByRoom.equals(4)) {
                return ApiResponse.failResponse();
            }
            if (roomType.equals(RoomType.SINGLE) && countByRoom.equals(2)) {
                return ApiResponse.failResponse();
            }
            // User가 참여한 방이 있을 경우
            Integer countByUser = userRoomRepository.countUserRoomsByUserId(user);
            if (countByUser >= 1) {
                return ApiResponse.failResponse();
            }

            Integer redTeamCount = userRoomRepository.countUserRoomsByRoomIdAndTeam(room, Team.RED);
            Integer blueTeamCount = userRoomRepository.countUserRoomsByRoomIdAndTeam(room, Team.BLUE);
            Team assignTeam = getTeam(roomType, redTeamCount, blueTeamCount);

            UserRoom userRoom = UserRoom.builder()
                    .roomId(room)
                    .userId(user)
                    .team(assignTeam)
                    .build();
            userRoomRepository.save(userRoom);

            return ApiResponse.successResponse(null);
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }

    // 들어갈 team 결정
    private static Team getTeam(RoomType roomType, Integer redTeamCount, Integer blueTeamCount) {
        if (roomType.equals(RoomType.DOUBLE)) {
            if (redTeamCount.equals(2)) {
                return Team.BLUE;
            } else if (blueTeamCount.equals(2)) {
                return Team.RED;
            } else {
                return Team.RED;
            }
        } else {
            if (redTeamCount.equals(1)) {
                return Team.BLUE;
            } else {
                return Team.RED;
            }
        }
    }

    // 방 나가기
    @Override
    @Transactional
    public ApiResponse<Object> exitRoom(Integer roomId, Integer userId) {
        try {
            Optional<Room> findRoom = roomRepository.findById(roomId);
            Optional<User> findUser = userRepository.findById(userId);

            if (findRoom.isEmpty() || findUser.isEmpty()) {
                return ApiResponse.failResponse();
            }
            Room room = findRoom.get();
            User user = findUser.get();

            // User가 Room에 참가한 상태일 때만 나가기 가능
            Optional<UserRoom> findUserRoom = userRoomRepository.findUserRoomByRoomIdAndUserId(room, user);
            if (findUserRoom.isEmpty()) {
                return ApiResponse.failResponse();
            }
            // WAIT 상태의 방만 나가기 가능
            if (!room.getStatus().equals(RoomStatus.WAIT)) {
                return ApiResponse.failResponse();
            }

            if (room.getHostId().equals(user)) {
                userRoomRepository.deleteUserRoomsByRoomId(room);
                LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                Room saveRoom = Room.builder()
                        .id(roomId)
                        .title(room.getTitle())
                        .host(user)
                        .room_type(room.getRoomType())
                        .status(RoomStatus.FINISH)
                        .created_at(room.getCreatedAt())
                        .updated_at(now)
                        .build();
                roomRepository.save(saveRoom);
            } else {
                userRoomRepository.deleteUserRoomByRoomIdAndUserId(room, user);
            }

            return ApiResponse.successResponse(null);

        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }
}
