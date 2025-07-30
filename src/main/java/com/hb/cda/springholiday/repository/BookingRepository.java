package com.hb.cda.springholiday.repository;

import com.hb.cda.springholiday.entity.Booking;
import com.hb.cda.springholiday.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUser(User user);
}
