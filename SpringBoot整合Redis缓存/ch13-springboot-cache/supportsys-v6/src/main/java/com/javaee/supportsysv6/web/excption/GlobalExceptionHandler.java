package com.javaee.supportsysv6.web.excption;


import com.javaee.supportsysv6.web.domain.bean.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * description: 全局异常处理类，包含Service异常处理、参数校验异常、Http方法异常和通用异常处理方法
 *
 * @author: hevean
 * @date: 2022/04/05
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 通用的服务层异常处理
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResultBean handleServiceException(ServiceException e) {
        log.error("业务层异常:" + e.toString() + "_" + e.getMessage(), e);
        ResultBean result = new ResultBean();
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
        return result;
    }

    /**
     * 处理参数校验不合法异常1
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultBean handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验失败:" + e.toString() + "_" + e.getMessage(), e);

        // 获取参数错误提示信息
        List<String> errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(s -> s.getField() + ":" + s.getDefaultMessage()).collect(Collectors.toList());

        return ResultBean.systemException(GlobalCodeEnum.FAIL_9997, errorMessage);
    }

    /**
     * 处理参数校验不合法异常2
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultBean handleBindExceptionException(BindException e) {
        log.error("参数校验异常:" + e.toString() + "_" + e.getMessage(), e);

        // 获取参数错误提示信息
        List<String> errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(s ->  s.getField()+ ":"+ s.getDefaultMessage())
                .collect(Collectors.toList());

        return ResultBean.systemException(GlobalCodeEnum.FAIL_9997, errorMessage);
    }


    /**
     * 处理接口请求方法不合法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultBean handleMethodNotSupportExceptionException(HttpRequestMethodNotSupportedException e) {
        // log.error("Method方法异常:" + e.toString() + "_" + e.getMessage(), e);

        // 获取参数错误提示信息
        String errorMessage = "此接口不支持" + e.getMethod();

        return ResultBean.systemException(GlobalCodeEnum.FAIL_9997, errorMessage);
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBean handleDefaultException(Exception e) {
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResultBean.fail(e.getMessage());
    }

}
