package prograpy.quest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User hostId;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private LocalDateTime createdAt;
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
