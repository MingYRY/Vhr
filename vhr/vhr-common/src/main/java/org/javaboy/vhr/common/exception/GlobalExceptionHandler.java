package org.javaboy.vhr.common.exception;

import org.javaboy.vhr.common.api.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 这里处理全局异常，使用 @ControllerAdvice+@ExceptionHandler注解能够进行近似全局异常处理
 * 能处理DispatcherServlet.doDispatch方法中DispatcherServlet.processDispatchResult方法之前捕捉到的所有异常，
 * 包括：拦截器、参数绑定（参数解析、参数转换、参数校验）、控制器、返回值处理等模块抛出的异常。
 * （@RestControllerAdvice=@ControllerAdvice+@ResponseBody），返回值自动为JSON的形式。
 * ExceptionHandler(里面放入什么异常，他就捕获什么类型的异常，然后返回给客户端)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有关联数据，操作失败!");
        }
        return RespBean.error("数据库异常，操作失败!");
    }
}