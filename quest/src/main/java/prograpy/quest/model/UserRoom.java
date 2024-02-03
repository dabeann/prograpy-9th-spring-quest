package prograpy.quest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoom {

    @Id
    @Column(name = "user_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "고유 아이디")
    private Integer id;

    // N:1 room
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "해당 방 아이디")
    @JoinColumn(name = "room_id")
    private Room roomId;

    // N:1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "해당 유저 아이디")
    private User userId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "속한 팀: 레드, 블루")
    private Team team;

    public enum Team {
        RED,
        BLUE
    }

    @Builder
    public UserRoom(Integer id, Room roomId, User userId, Team team) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.team = team;
    }
}
