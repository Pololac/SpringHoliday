package com.hb.cda.springholiday.controller.dto;

import java.time.LocalDate;
import java.util.List;

public class BookingDTO {
    private String id;
    private List<RoomDTO> rooms;
    private Integer guestCount;
    private UserDTO user;
    private Double total;
    private LocalDate startDate;
    private LocalDate endDate;

    public BookingDTO() {
    }

    public BookingDTO(String id, List<RoomDTO> rooms, Integer guestCount, UserDTO user, Double total, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.rooms = rooms;
        this.guestCount = guestCount;
        this.user = user;
        this.total = total;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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
