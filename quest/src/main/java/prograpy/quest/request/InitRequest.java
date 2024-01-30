package prograpy.quest.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitRequest {

    private int seed;
    private int quantity;
}
