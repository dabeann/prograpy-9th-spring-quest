package prograpy.quest.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomCreateRequest {

    private int userId;
    private String roomType;
    private String title;
}
