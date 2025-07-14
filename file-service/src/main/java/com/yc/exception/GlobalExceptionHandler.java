//package com.yc.exception;
//
//import com.core.utils.Result;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * 全局异常处理器
// */
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public Result handleIllegalArgumentException(IllegalArgumentException e) {
//        return Result.error(e.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public Result handleGeneralException(Exception e) {
//        return Result.error("服务器内部错误：" + e.getMessage());
//    }
//}
