package prograpy.quest.model;

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
    private Integer id;

    // N:1 room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room roomId;

    // N:1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Enumerated(EnumType.STRING)
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
