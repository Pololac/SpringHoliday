package com.hb.cda.springholiday.controller.dto;


public class SimpleMessageDTO {
    private String Message;

    public SimpleMessageDTO(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
