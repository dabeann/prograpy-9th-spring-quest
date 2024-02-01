package prograpy.quest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import prograpy.quest.model.Room;
import prograpy.quest.model.Room.RoomStatus;
import prograpy.quest.model.Room.RoomType;
import prograpy.quest.model.User;
import prograpy.quest.model.User.UserStatus;
import prograpy.quest.repository.RoomRepository;
import prograpy.quest.repository.UserRepository;
import prograpy.quest.repository.UserRoomRepository;
import prograpy.quest.request.InitRequest;
import prograpy.quest.response.ApiResponse;
import prograpy.quest.response.FakerApiResponse;
import prograpy.quest.response.FakerApiResponse.UserData;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final ObjectMapper objectMapper;

    // 게임시작 후 1분 뒤 FINISH로 변경
    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void finishGame() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
        roomRepository.updateRoomStatusAfterTime(RoomStatus.FINISH, now, oneMinuteAgo, RoomStatus.PROGRESS);

        // Room status가 FINISH이면 UserRoom 모두 삭제
        Optional<Room> finishRoom = roomRepository.findRoomByStatus(RoomStatus.FINISH);
        if (finishRoom.isPresent()) {
            userRoomRepository.deleteUserRoomsByRoomId(finishRoom.get());
        }
    }

    // 헬스체크
    @Override
    public ApiResponse<Object> healthCheck() {
        try {
            return ApiResponse.successResponse(null);
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }

    @Value("${faker.url}")
    private String fakerApiUrl;
    // 초기화
    @Override
    public ApiResponse<Object> init(InitRequest initRequest) {
        // 모든 table 의 data 삭제
        userRoomRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();

        int seed = initRequest.getSeed();
        int quantity = initRequest.getQuantity();

        try {
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type을 JSON으로 설정

            Map<String , Object> map = new HashMap<>();
            map.put("seed", seed);
            map.put("quantity", quantity);
            String params1 = objectMapper.writeValueAsString(map);

            HttpEntity<String> requestEntity = new HttpEntity<>(params1, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FakerApiResponse> apiResponse = restTemplate.exchange(
                    fakerApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    FakerApiResponse.class);
            List<FakerApiResponse.UserData> apiData;

            if (apiResponse.getBody() != null && apiResponse.getBody().getCode() == 200){
                apiData = apiResponse.getBody().getData();
                apiData.sort(Comparator.comparing(UserData::getId));

                for (FakerApiResponse.UserData response : apiData) {
                    User user = processApiResponse(response);
                    userRepository.save(user);
                }
                return ApiResponse.successResponse(null);
            } else{
                return ApiResponse.errorResponse();
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }

    // 게임시작
    @Override
    public ApiResponse<Object> gameStart(Integer roomId, Integer userId) {
        try {
            Optional<Room> findRoom = roomRepository.findById(roomId);
            Optional<User> findUser = userRepository.findById(userId);

            if (findRoom.isEmpty() || findUser.isEmpty()) {
                return ApiResponse.failResponse();
            }
            Room room = findRoom.get();
            User user = findUser.get();

            if (!room.getHostId().equals(user)){
                return ApiResponse.failResponse();
            }
            // 방 정원이 꽉 찬 상태에서만 게임 시작 가능
            Integer countByRoom = userRoomRepository.countUserRoomsByRoomId(room);
            if (room.getRoomType().equals(RoomType.DOUBLE) && !countByRoom.equals(4)) {
                return ApiResponse.failResponse();
            } else if (room.getRoomType().equals(RoomType.SINGLE) && !countByRoom.equals(2)) {
                return ApiResponse.failResponse();
            }
            if (!room.getStatus().equals(RoomStatus.WAIT)) {
                return ApiResponse.failResponse();
            }

            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Room saveRoom = Room.builder()
                    .id(roomId)
                    .title(room.getTitle())
                    .host(user)
                    .room_type(room.getRoomType())
                    .status(RoomStatus.PROGRESS)
                    .created_at(room.getCreatedAt())
                    .updated_at(now)
                    .build();
            roomRepository.save(saveRoom);

            return ApiResponse.successResponse(null);
        } catch (Exception e) {
            return ApiResponse.errorResponse();
        }
    }

    private User processApiResponse(FakerApiResponse.UserData response) {
        UserStatus userStatus;
        if (response.getId() <= 30) {
            userStatus = UserStatus.ACTIVE;
        } else if (response.getId() <= 60) {
            userStatus = UserStatus.WAIT;
        } else {
            userStatus = UserStatus.NON_ACTIVE;
        }
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return User.builder()
                .fakerId(Math.toIntExact(response.getId()))
                .name(response.getUsername())
                .email(response.getEmail())
                .status(userStatus)
                .created_at(now)
                .updated_at(now)
                .build();
    }
}
