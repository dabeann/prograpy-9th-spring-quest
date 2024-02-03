package prograpy.quest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "방")
public class Room {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "고유 아이디")
    private Integer id;

    @Schema(description = "제목")
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "호스트 아이디")
    private User hostId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "방 유형: 단식, 복식")
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Schema(description = "방 상태: 대기, 진행중, 완료")
    private RoomStatus status;

    @Schema(description = "생성된 시간", example = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "변경된 시간", example = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public enum RoomType {
        SINGLE, // 단식
        DOUBLE // 복식
    }

    public enum RoomStatus {
        WAIT, // 대기
        PROGRESS, // 진행중
        FINISH // 완료
    }

    @Builder
    public Room(Integer id, String title, User host, RoomType room_type, RoomStatus status, LocalDateTime created_at,
                LocalDateTime updated_at) {
        this.id = id;
        this.title = title;
        this.hostId = host;
        this.roomType = room_type;
        this.status = status;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }
}
