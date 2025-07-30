package com.hb.cda.springholiday.controller;

import com.hb.cda.springholiday.business.exception.BookingException;
import com.hb.cda.springholiday.business.exception.BusinessException;
import com.hb.cda.springholiday.business.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail userExists(UserAlreadyExistsException e){
        return ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(BookingException.class)
    public ProblemDetail bookingError(BookingException e){
        return ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    //  @ExceptionHandler(ExampleException.class)
    // public ProblemDetail example(ExampleException e){
    //         return ProblemDetail
    //         .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    // }

}

