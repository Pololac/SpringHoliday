package com.hb.cda.springholiday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String number;
    private Double price;
    private Integer capacity;

    @ManyToMany(mappedBy = "rooms")
    private List<Booking> bookings = new ArrayList<>();

    public Room() {
    }

    public Room(String id, String number, Double price, Integer capacity, List<Booking> bookings) {
        this.id = id;
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.bookings = bookings;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}



