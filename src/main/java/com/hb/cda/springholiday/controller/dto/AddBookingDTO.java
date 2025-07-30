package com.hb.cda.springholiday.controller.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class AddBookingDTO {
    @NotNull
    @Positive
    private Integer guestCount;
    @NotEmpty
    private List<RoomDTO> rooms;
    @NotNull
    @FutureOrPresent
    private LocalDate startDate;
    @NotNull
    @Future
    private LocalDate endDate;

    public AddBookingDTO() {
    }

    public AddBookingDTO(Integer guestCount, List<RoomDTO> rooms, LocalDate startDate, LocalDate endDate) {
        this.guestCount = guestCount;
        this.rooms = rooms;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
