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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
