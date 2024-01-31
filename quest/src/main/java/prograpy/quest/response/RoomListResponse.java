package prograpy.quest.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

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
    }
}
