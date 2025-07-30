package com.hb.cda.springholiday.repository;

import com.hb.cda.springholiday.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Optional<Room> findByNumber(String number);

    @Query("""
        SELECT count(b) = 0 FROM Room r
            JOIN r.bookings b
            WHERE b.startDate < :end
            AND b.endDate >:start
            AND r = :room
       """)
    boolean isRoomAvailable(Room room, LocalDate start, LocalDate end);

    @Query("""
        FROM Room r WHERE r NOT IN (
            SELECT b.rooms FROM Booking b
            WHERE b.startDate < :end
            AND b.endDate >:start)
        """)
    List<Room> findAvailable(LocalDate start, LocalDate end);


}
