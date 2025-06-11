package com.core.exception;

import com.core.utils.Result;
import com.core.utils.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.nio.file.AccessDeniedException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有未被捕获的异常（最通用的兜底异常处理）
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("全局异常捕获 : {}", ex.getMessage(), ex);
        Result result = Result.error(ResultEnum.error);
        result.setMsg("系统异常: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.append(error.getField())
                        .append(": ").append(error.getDefaultMessage()).append(", "));
        Result result = Result.error(HttpStatus.BAD_REQUEST.value(), "参数校验异常: " + errors);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result> handleRuntimeExceptions(RuntimeException ex) {
        Result result = Result.error(HttpStatus.BAD_REQUEST.value(), "运行时异常: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException ex) {
        Result result = Result.error("权限不足");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }
}
