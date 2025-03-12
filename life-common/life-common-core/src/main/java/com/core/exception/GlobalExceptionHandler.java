package com.core.exception;

import com.core.utils.Result;
import com.core.utils.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //捕获全部异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleAllExceptions(Exception ex, WebRequest request) {
        Result result = Result.error(ResultEnum.error);
        result.setMsg("错误发生原因：: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(", "));
        Result result = Result.error(HttpStatus.BAD_REQUEST.value(), errors.toString());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 处理自定义异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result> handleRuntimeExceptions(RuntimeException ex) {
        Result result = Result.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}



