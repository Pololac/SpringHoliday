package com.hb.cda.springholiday.business.impl;

import com.hb.cda.springholiday.business.BookingBusiness;
import com.hb.cda.springholiday.business.exception.BookingException;
import com.hb.cda.springholiday.entity.Booking;
import com.hb.cda.springholiday.entity.Room;
import com.hb.cda.springholiday.entity.User;
import com.hb.cda.springholiday.repository.BookingRepository;
import com.hb.cda.springholiday.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingBusinessImpl implements BookingBusiness {
    private RoomRepository roomRepo;
    private BookingRepository bookingRepo;

    public BookingBusinessImpl(RoomRepository roomRepo, BookingRepository bookingRepo) {
        this.roomRepo = roomRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        return roomRepo.findAvailable(startDate, endDate);
    }

    @Override
    public Booking bookRooms(Booking booking, User user) {
        int totalCapacity = 0;
        double totalPrice = 0.0;
        long dayCount = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());

        // Calcul de la capacité et du prix total des chambres réservées
        for (Room room : booking.getRooms()) {
            // On vérifie que la room envoyée en requete existe bien et on se base sur elle
            // pour faire tous les tests et calculs car c'est la seule donnée fiable
            Room persistedRoom = roomRepo.findById(room.getId())
                    .orElseThrow(() -> new BookingException("Room" + room.getNumber() + " not found."));

            if(!roomRepo.isRoomAvailable(room, booking.getStartDate(), booking.getEndDate())) {
                throw new BookingException("Room " + room.getNumber() + " is not available for the selected period.");
            }
            totalCapacity += persistedRoom.getCapacity();
            totalPrice += persistedRoom.getPrice() * dayCount;
        }

        // Vérification que la capacité recherchée est inférieure à la capacité des chambres; sinon erreur
        if(booking.getGuestCount() > totalCapacity) {
            throw new BookingException("The requested guest count is greater than the total capacity of the selected rooms.");
        }

        // Assignation des valeurs et enregistrement en BDD
        booking.setUser(user);
        booking.setTotal(totalPrice);
        bookingRepo.save(booking);
        return booking;
    }
}
