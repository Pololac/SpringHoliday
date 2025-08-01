package com.hb.cda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.hb.cda.springholiday.entity.Booking;
import com.hb.cda.springholiday.entity.Room;
import com.hb.cda.springholiday.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {"com.hb.cda.springholiday.entity", "com.hb.cda.springholiday.repository"})
public class DataLoader implements CommandLineRunner {
    @Autowired
    private EntityManager em;

    public List<String> roomsId = new ArrayList<>();

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(DataLoader.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        em.createQuery("DELETE FROM Booking").executeUpdate();
        em.createQuery("DELETE FROM Room").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        User user = new User(null, "user@test.com", "$2y$10$CAH8CtyiTrfHuQLHXHG4sujVuO4a4rF91B.VbQXf12CCbGlfRuHu.",
                "ROLE_USER", true, new ArrayList<>());
        User admin = new User(null, "admin@test.com", "$2y$10$CAH8CtyiTrfHuQLHXHG4sujVuO4a4rF91B.VbQXf12CCbGlfRuHu.",
                "ROLE_ADMIN", true, new ArrayList<>());
        em.persist(user);
        em.persist(admin);

        List<Room> rooms = List.of(
                new Room(null, "1A", 65.0, 2, new ArrayList<>()),
                new Room(null, "1B", 85.0, 3, new ArrayList<>()),
                new Room(null, "2A", 65.0, 2, new ArrayList<>()),
                new Room(null, "2B", 125.0, 4, new ArrayList<>()));

        rooms.stream().forEach(item -> em.persist(item));

        List<Booking> bookings = List.of(
                new Booking(null, LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 14), 130.0, 2, rooms.subList(0, 1), user),
                new Booking(null, LocalDate.of(2025, 8, 25), LocalDate.of(2025, 8, 26), 140.0, 5, rooms.subList(0, 2), user));

        bookings.stream().forEach(item -> em.persist(item));

        roomsId = rooms.stream().map(item -> item.getId()).toList();
    }

}

