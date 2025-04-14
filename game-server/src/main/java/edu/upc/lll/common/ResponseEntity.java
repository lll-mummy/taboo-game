package edu.upc.lll.common;

import edu.upc.lll.common.exception.ErrorCodeEnum;
import lombok.Data;


@Data
public class ResponseEntity<T> {
    private Integer code;
    private String message;
    private T data;


    public static <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(ErrorCodeEnum.SUCCESS.code());
        result.setMessage(ErrorCodeEnum.SUCCESS.message());
        result.setData(data);
        return result;
    }

    // 使用ErrorCodeEnum来返回错误响应
    public static <T> ResponseEntity<T> error(ErrorCodeEnum errorCode) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(errorCode.code());
        result.setMessage(errorCode.message());
        return result;
    }

    // 使用ErrorCodeEnum来返回带有自定义消息的错误响应
    public static <T> ResponseEntity<T> error(ErrorCodeEnum errorCode, String customMessage) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(errorCode.code());
        result.setMessage(customMessage != null ? customMessage : errorCode.message());
        return result;
    }
}
