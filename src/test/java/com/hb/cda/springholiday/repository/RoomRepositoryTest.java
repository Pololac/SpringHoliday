package com.hb.cda.springholiday.repository;

import com.hb.cda.DataLoader;
import com.hb.cda.springholiday.entity.Room;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoomRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    private RoomRepository roomRepository;

    Room testRoom;

    @BeforeEach
    void setUp() throws Exception {
        DataLoader dl = new DataLoader();
        dl.setEm(em);
        dl.run();
        testRoom = roomRepository.findByNumber("1A").get();
    }

    // Vérif bon chargement des infos du DataLoader
    @Test
    void countRoom() {
        assertNotNull(testRoom);
        assertEquals(4, roomRepository.count());
    }

    @ParameterizedTest
    @MethodSource("roomAvailableSource")
    void isRoomAvailableShouldReturnTrueIfNoBookingForGivenDates(LocalDate start, LocalDate end, boolean expected) {
        assertEquals(expected,
                roomRepository.isRoomAvailable(
                        testRoom,
                        start,
                        end
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findAvailableSource")
    void findAvailableShouldReturnRoomsWithoutBookingForGivenDates(LocalDate start, LocalDate end, int exepectedCount) {
        List<Room> rooms = roomRepository.findAvailable(
                start, end);
        assertEquals(exepectedCount, rooms.size());
    }

    static Stream<Arguments> roomAvailableSource() {
        return Stream.of(
                Arguments.of(LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 12), true),
                Arguments.of(LocalDate.of(2025, 8, 8), LocalDate.of(2025, 8, 9), true),
                Arguments.of(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 11), false),
                Arguments.of(LocalDate.of(2025, 8, 11), LocalDate.of(2025, 8, 12), false),
                Arguments.of(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 14), false),
                Arguments.of(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 18), false),
                Arguments.of(LocalDate.of(2025, 8, 13), LocalDate.of(2025, 8, 18), false)
        );
    }

    static Stream<Arguments> findAvailableSource() {
        return Stream.of(
                Arguments.of(LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 12), 4),
                Arguments.of(LocalDate.of(2025, 8, 8), LocalDate.of(2025, 8, 9), 4),
                Arguments.of(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 11), 3),
                Arguments.of(LocalDate.of(2025, 8, 11), LocalDate.of(2025, 8, 12), 3),
                Arguments.of(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 14), 3),
                Arguments.of(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 18), 3),
                Arguments.of(LocalDate.of(2025, 8, 13), LocalDate.of(2025, 8, 18), 3),
                Arguments.of(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 30), 2),
                Arguments.of(LocalDate.of(2025, 8, 20), LocalDate.of(2025, 8, 30), 2)
        );
    }
}

