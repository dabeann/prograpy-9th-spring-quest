package prograpy.quest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "faker_id")
    private Integer fakerId;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public enum UserStatus {
        WAIT, // 대기
        ACTIVE, // 활성
        NON_ACTIVE // 비활성
    }

    @Builder
    private User(Integer id, Integer fakerId, String name, String email, UserStatus status,
                 LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.fakerId = fakerId;
        this.name = name;
        this.email = email;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
