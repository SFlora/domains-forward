package com.ueelab.domainforward;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ueelab
 */
@ControllerAdvice
public class IgnoreExceptionHandler {
    
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    String ignore() {
        return "";
    }
    
}
