package com.hb.cda.springholiday.controller.dto.mapper;

import com.hb.cda.springholiday.controller.dto.AddBookingDTO;
import com.hb.cda.springholiday.controller.dto.BookingDTO;
import com.hb.cda.springholiday.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {
    BookingDTO convertToDTO(Booking booking);
    Booking convertToEntity(AddBookingDTO dto);
}
