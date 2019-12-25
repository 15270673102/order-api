package com.loushi.component.annotation;

import com.google.common.base.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * @author 技术部
 */
public class DateValidator implements ConstraintValidator<DateVid, String> {

    private String regexp;
    private String message;

    @Override
    public void initialize(DateVid parameters) {
        regexp = parameters.regexp();
        message = parameters.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Strings.isNullOrEmpty(value)) {
            return true;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(regexp);
            sdf.parse(value);

        } catch (ParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }

}
