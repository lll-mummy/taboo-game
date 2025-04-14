package edu.upc.lll.common.exception;

import edu.upc.lll.common.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验异常（例如 @Valid 失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        // 循环输出错误字段信息
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String errorDetail = fieldError.getDefaultMessage();
            errorMsg.append(errorDetail).append("; ");
            log.error("参数校验失败 - " + fieldError.getField() + ": " + errorDetail);
        }
        return ResponseEntity.error(ErrorCodeEnum.VALIDATION_ERROR, errorMsg.toString());
    }

    /**
     * 自定义业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        log.error("BusinessException caught: Code = {}, Message = {}", ex.getCode(), ex.getMessage());
        return ResponseEntity.error(ErrorCodeEnum.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("系统异常: ", ex);
        return ResponseEntity.error(ErrorCodeEnum.SERVER_ERROR, "系统异常，请联系管理员");
    }
}
