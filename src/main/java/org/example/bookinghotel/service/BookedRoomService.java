package org.example.bookinghotel.service;


import org.example.bookinghotel.dto.BookingRoomDto;
import org.example.bookinghotel.model.BookedRoom;
import org.example.bookinghotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookedRoomService extends GeneralService<BookedRoom> {
    Page<BookedRoom> findAll(Pageable pageable);
    Page<BookedRoom> findAll(PaginateRequest paginateRequest , LocalDate checkIn, LocalDate checkOut, String sortField , String sort);
    Optional<BookedRoom> findById(Long id);
    BookedRoom toBookedRoom(BookingRoomDto bookingRoomDto);
    boolean checkDate(LocalDate checkInDate, LocalDate checkOutDate);
    void edit(BookedRoom bookedRoom, Long id);
}
