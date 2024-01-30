package prograpy.quest.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import prograpy.quest.model.User;

@Getter
@Builder
public class UserListResponse {
    private int totalElements;
    private int totalPages;
    private List<User> userList;
}
