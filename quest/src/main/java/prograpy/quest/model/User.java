package prograpy.quest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
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
    private UserStatus status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public enum UserStatus {
        WAIT, // 대기
        ACTIVE, // 활성
        NON_ACTIVE // 비활성
    }
}
