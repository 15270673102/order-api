package com.loushi.component.exception;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.loushi.util.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理参数异常类
 * @author 技术部
 */

@RestControllerAdvice
@Slf4j
public class ExceptionHanlder {

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleBindException(BindException ex) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null;

        String str = StrUtil.format("{}=[{}],{}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        log.error("--> {}", str);
        return AjaxResult.error400(str);
    }


    /**
     * 500 - Internal Server Error
     */
    private static Pattern matcherChinese = Pattern.compile("[\u4e00-\u9fa5]");

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleException(Exception e) {
        log.error("--> {}", e.getMessage());

        //匹配是否有中文，没有跳过
        if (ReUtil.contains(matcherChinese, e.getMessage())) {
            return AjaxResult.error500(e.getMessage());
        }
        e.printStackTrace();
        return AjaxResult.error500();
    }

}

