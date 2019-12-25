package com.loushi.component.annotation;

import com.alibaba.fastjson.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @author 技术部
 */
public class ListStrValidator implements ConstraintValidator<ListStrVid, String> {

    private String message;

    @Override
    public void initialize(ListStrVid parameters) {
        message = parameters.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            JSONObject.parseArray(value);

        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }

}
