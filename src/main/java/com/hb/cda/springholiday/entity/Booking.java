package com.hb.cda.springholiday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double total;
    private Integer guestCount;

    @ManyToMany
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id") // Nom de la colonne en base, peut être omis (JPA le devine)
    private User user;

    public Booking() {
    }

    public Booking(String id, LocalDate startDate, LocalDate endDate, Double total, Integer guestCount, List<Room> rooms, User user) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
        this.guestCount = guestCount;
        this.rooms = rooms;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
