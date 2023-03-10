package com.hitd.im.service.exception;


import com.hitd.im.common.BaseErrorCode;
import com.hitd.im.common.R;
import com.hitd.im.common.exception.ApplicationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public R<?> unKnowExceptionHandler(Exception e){
        e.printStackTrace();
        R<?> resultBean =new R<>();
        resultBean.setCode(BaseErrorCode.SYSTEM_ERROR.getCode());
        resultBean.setMsg(BaseErrorCode.SYSTEM_ERROR.getError());

        /*
         * 系统异常，记入异常日志，可拓展为邮件、短信等提醒运维人员
         */

        return resultBean;
    }


    /**
     * Validator 参数校验异常处理
     *
     * @param ex 单个参数异常
     * @return R
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        R<?> resultBean =new R<>();
        resultBean.setCode(BaseErrorCode.PARAMETER_ERROR.getCode());
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
            // 读取参数字段，constraintViolation.getMessage() 读取验证注解中的message值
            String paramName = pathImpl.getLeafNode().getName();
            String message = "参数{".concat(paramName).concat("}").concat(constraintViolation.getMessage());
            resultBean.setMsg(message);
            return resultBean;
        }
        resultBean.setMsg(BaseErrorCode.PARAMETER_ERROR.getError() + ex.getMessage());
        return resultBean;
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public R<?> applicationExceptionHandler(ApplicationException e) {
        // 使用公共的结果类封装返回结果, 这里我指定状态码为
        R<?> resultBean =new R<>();
        resultBean.setCode(e.getCode());
        resultBean.setMsg(e.getError());
        return resultBean;
    }

    /**
     * Validator 参数校验异常处理
     *
     * @param ex 多个参数异常
     * @return R
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public R<?>  bindExceptionHandler(BindException ex) {
        FieldError err = ex.getFieldError();
        String message = "参数{".concat(err.getField()).concat("}").concat(err.getDefaultMessage());
        R<?> resultBean =new R<>();
        resultBean.setCode(BaseErrorCode.PARAMETER_ERROR.getCode());
        resultBean.setMsg(message);
        return resultBean;
    }

    //json格式
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R<?>  methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        StringBuilder errorMsg = new StringBuilder();
        BindingResult re = ex.getBindingResult();
        for (ObjectError error : re.getAllErrors()) {
            errorMsg.append(error.getDefaultMessage()).append(",");
        }
        errorMsg.delete(errorMsg.length() - 1, errorMsg.length());

        R<?> resultBean =new R<>();
        resultBean.setCode(BaseErrorCode.PARAMETER_ERROR.getCode());
        resultBean.setMsg(BaseErrorCode.PARAMETER_ERROR.getError() + " : " + errorMsg);
        return resultBean;
    }

}
