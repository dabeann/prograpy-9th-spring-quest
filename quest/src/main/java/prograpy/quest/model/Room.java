package prograpy.quest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User host;

    @Enumerated(EnumType.STRING)
    private RoomType room_type;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

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
        this.host = host;
        this.room_type = room_type;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
