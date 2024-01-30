package prograpy.quest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(Include.NON_NULL) // null로 들어오는 값은 제외
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T result;

    private static final Integer SUCCESS_CODE = 200;
    private static final Integer FAIL_CODE = 201;
    private static final Integer ERROR_CODE = 500;

    private static final String SUCCESS_MESSAGE = "API 요청이 성공했습니다.";
    private static final String FAIL_MESSAGE = "불가능한 요청입니다.";
    private static final String ERROR_MESSAGE = "에러가 발생했습니다.";

    public static <T> ApiResponse<T> successResponse(T result) {
        return ApiResponse.<T>builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> failResponse() {
        return ApiResponse.<T>builder()
                .code(FAIL_CODE)
                .message(FAIL_MESSAGE)
                .build();
    }

    public static <T> ApiResponse<T> errorResponse() {
        return ApiResponse.<T>builder()
                .code(ERROR_CODE)
                .message(ERROR_MESSAGE)
                .build();
    }

    @Builder
    private ApiResponse(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
