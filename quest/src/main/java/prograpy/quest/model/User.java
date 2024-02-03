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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "고유 아이디")
    private Integer id;

    @Column(name = "faker_id")
    @Schema(description = "외부 api 아이디")
    private Integer fakerId;

    @Schema(description = "이름", example = "jungran.gwon")
    private String name;

    @Schema(description = "이메일", example = "knam@example.com")
    private String email;

    @Schema(description = "유저 상태: 대기, 활성, 비활성")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Schema(description = "생성된 시간", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "수정된 시간", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

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
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }
}
