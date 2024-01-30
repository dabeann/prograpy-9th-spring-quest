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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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
}
