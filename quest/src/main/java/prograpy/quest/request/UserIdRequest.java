package prograpy.quest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserIdRequest {
    @Schema(description = "유저 고유 아이디")
    Integer userId;
}
