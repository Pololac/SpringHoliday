package com.hb.cda.springholiday.business.exception;

public class UserAlreadyExistsException extends BusinessException{
    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
