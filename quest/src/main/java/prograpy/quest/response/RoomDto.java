package prograpy.quest.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import prograpy.quest.model.Room;

@Getter
@Builder
public class RoomDto {
    private Integer id;
    private String title;
    private int hostId;
    private String roomType;
    private String status;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static RoomDto fromEntity(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .hostId(room.getHostId().getId())
                .roomType(String.valueOf(room.getRoomType()))
                .status(String.valueOf(room.getStatus()))
                .createdAt(formatDateTime(room.getCreatedAt()))
                .updatedAt(formatDateTime(room.getUpdatedAt()))
                .build();
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
