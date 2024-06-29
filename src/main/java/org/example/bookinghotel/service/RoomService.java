package org.example.bookinghotel.service;


import org.example.bookinghotel.model.Room;
import org.example.bookinghotel.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface RoomService extends GeneralService<Room> {
    Page<Room> findAll(PaginateRequest paginateRequest ,String roomType, Long roomPrice, String sortField , String sort);
    Optional<Room> findById(Long id);
    Page<Room> findAll(Pageable pageable);
    Room checkRoom(Room room);
}
