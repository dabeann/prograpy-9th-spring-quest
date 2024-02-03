package prograpy.quest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomCreateRequest {

    @Schema(description = "호스트 아이디")
    private int userId;

    @Schema(description = "방 유형: 단식, 복식")
    private String roomType;

    @Schema(description = "방 제목")
    private String title;
}
