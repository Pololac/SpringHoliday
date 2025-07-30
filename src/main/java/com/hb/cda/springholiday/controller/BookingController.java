package com.hb.cda.springholiday.controller;

import com.hb.cda.springholiday.business.BookingBusiness;
import com.hb.cda.springholiday.controller.dto.AddBookingDTO;
import com.hb.cda.springholiday.controller.dto.BookingDTO;
import com.hb.cda.springholiday.controller.dto.mapper.BookingMapper;
import com.hb.cda.springholiday.entity.Booking;
import com.hb.cda.springholiday.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bookings")
public class BookingController {
    private BookingBusiness bookingBusiness;
    private BookingMapper mapper;

    public BookingController(BookingBusiness bookingBusiness, BookingMapper mapper) {
        this.bookingBusiness = bookingBusiness;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // @Secured("ROLE USER") // Si on préfère faire la gestion des accès sur les méthodes/contrôleurs
    public BookingDTO bookRooms(@RequestBody @Valid AddBookingDTO dto, @AuthenticationPrincipal User user) {
        Booking booking = bookingBusiness.bookRooms(mapper.convertToEntity(dto), user);
        return mapper.convertToDTO(booking);
    }
}
