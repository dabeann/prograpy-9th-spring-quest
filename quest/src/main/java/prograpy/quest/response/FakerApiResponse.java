package prograpy.quest.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FakerApiResponse {

    private String status;
    private Integer code;
    private Integer total;
    private List<UserData> data;

    @Getter
    @NoArgsConstructor
    public static class UserData{

        private Long id;
        private String uuid;
        private String firstname;
        private String lastname;
        private String username;
        private String password;
        private String email;
        private String ip;
        private String macAddress;
        private String website;
        private String image;
    }
}
