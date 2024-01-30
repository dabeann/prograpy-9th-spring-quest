package prograpy.quest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

            // Object Mapper를 통한 JSON 바인딩
            Map<String , Object> map = new HashMap<>();
            map.put("seed", seed);
            map.put("quantity", quantity);
            String params1 = objectMapper.writeValueAsString(map);

            // HttpEntity에 헤더 및 params 설정
            HttpEntity<String> requestEntity = new HttpEntity<>(params1, headers);

            // 외부 API에서 회원 정보 가져오기
            // RestTemplate의 exchange 메소드를 통해 URL에 HttpEntity와 함께 요청
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FakerApiResponse> apiResponse = restTemplate.exchange(
                    fakerApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    FakerApiResponse.class);
            List<FakerApiResponse.UserData> apiData;

            if (apiResponse.getBody() != null && apiResponse.getBody().getCode() == 200){
                apiData = apiResponse.getBody().getData();

                // id를 기준으로 오름차순 정렬
                apiData.sort(Comparator.comparing(UserData::getId));

                // 가져온 회원 정보 저장
                for (FakerApiResponse.UserData response : apiData) {
                    User user = processApiResponse(response);
                    userRepository.save(user);
                }
                return ApiResponse.successResponse(null);

            } else{
                return ApiResponse.errorResponse();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
