package org.example.bookinghotel.repository;



import org.example.bookinghotel.model.BookedRoom;
import org.example.bookinghotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedRoom, Long>, JpaSpecificationExecutor<BookedRoom> {
}