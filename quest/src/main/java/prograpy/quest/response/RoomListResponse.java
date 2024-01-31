package prograpy.quest.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import prograpy.quest.model.Room;

@Getter
@Builder
public class RoomListResponse {
    private int totalElements;
    private int totalPages;
    private List<PageRoom> roomList;

    @Getter
    @Builder
    public static class PageRoom{
        private int id;
        private String title;
        private int hostId;
        private String roomType;
        private String status;

        public static PageRoom fromEntity(Room room) {
            return PageRoom.builder()
                    .id(room.getId())
                    .title(room.getTitle())
                    .hostId(room.getHostId().getId())
                    .roomType(String.valueOf(room.getRoomType()))
                    .status(String.valueOf(room.getStatus()))
                    .build();
        }
    }
}
