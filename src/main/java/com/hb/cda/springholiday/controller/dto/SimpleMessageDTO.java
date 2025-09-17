package com.hb.cda.springholiday.controller.dto;


public class SimpleMessageDTO {
    private String message;

    public SimpleMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
