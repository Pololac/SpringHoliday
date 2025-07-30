package com.hb.cda.springholiday.controller.dto;

public class RoomDTO {
    private String id;
    private String number;
    private Integer capacity;
    private Double price;

    public RoomDTO() {
    }

    public RoomDTO(String id, String number, Integer capacity, Double price) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
