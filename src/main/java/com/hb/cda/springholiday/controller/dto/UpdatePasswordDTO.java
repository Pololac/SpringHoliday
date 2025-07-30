package com.hb.cda.springholiday.controller.dto;

public class UpdatePasswordDTO {
    private String newPassword;

    public UpdatePasswordDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
