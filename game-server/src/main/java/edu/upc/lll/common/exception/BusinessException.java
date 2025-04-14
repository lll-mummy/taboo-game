package edu.upc.lll.common.exception;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.message());
        this.code = errorCode.code();
    }
}

