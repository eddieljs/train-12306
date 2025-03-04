package com.eddie.train.common.controller;

import com.eddie.train.common.exception.BusinessException;
import com.eddie.train.common.resp.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 * @RestControllerAdvice
 * 它用于定义一个全局的控制器增强类。这个注解是 @ControllerAdvice 和 @ResponseBody 的组合，
 * 它既可以被用来定义全局的异常处理逻辑，也可以用来为所有控制器（Controller）的方法提供全局的数据预处理和后处理功能。
 */

@ControllerAdvice
//@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 所有异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
//        LOG.info("seata全局事务ID: {}", e);
//        如果是在一次全局事务里出异常了，就不要包装返回值，将异常抛给调用方，让调用方回滚事务
//        if (StrUtil.isNotBlank(RootContext.getXID())) {
//            throw e;
//        }
        LOG.error("系统异常：", e);
        return Result.error("系统异常");
    }

    /**
     * 数据重复输入异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        LOG.error("数据重复异常：", e);
        return Result.error("数据重复，请检查输入数据");
    }

    /**
     * 业务异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result exceptionHandler(BusinessException e) {
        LOG.error("业务异常：{}", e.getE().getDesc());
        return Result.error(e.getE().getDesc());
    }

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public Result exceptionHandler(BindException e) {
        LOG.error("校验异常：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result exceptionHandler(RuntimeException e) {
        throw e;
    }

}
