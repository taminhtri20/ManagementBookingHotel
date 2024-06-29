package org.example.bookinghotel.service.impl;

import org.example.bookinghotel.dto.BookingRoomDto;
import org.example.bookinghotel.model.BookedRoom;
import org.example.bookinghotel.model.User;
import org.example.bookinghotel.repository.BookingRepository;
import org.example.bookinghotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
@Service
public class BookedServiceImpl implements BookedRoomService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Override
    public Page<BookedRoom> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Page<BookedRoom> findAll(PaginateRequest paginateRequest, LocalDate checkIn, LocalDate checkOut, String sortField, String sort) {
        Pageable paging = null;
        if (sort.isEmpty() &&sortField.isEmpty()){
            paging = PageRequest.of(paginateRequest.getPage(),  paginateRequest.getSize());
        }else {
            Sort sort1 = sort.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
            paging = PageRequest.of(paginateRequest.getPage(),  paginateRequest.getSize(), sort1);
        }
        BookingSpec bookingSpec = new BookingSpec(new SearchCriteria("checkInDate", ">", checkIn));
        BookingSpec bookingSpec1 = new BookingSpec(new SearchCriteria("checkOutDate", "<", checkOut));
        Page<BookedRoom> bookedRooms = bookingRepository.findAll(Specification.where(bookingSpec).
                and(Specification.where(bookingSpec1)), paging);
        return bookedRooms;
    }

    @Override
    public Optional<BookedRoom> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public BookedRoom toBookedRoom(BookingRoomDto bookingRoomDto) {
        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setCheckInDate(bookingRoomDto.getCheckInDate());
        bookedRoom.setCheckOutDate(bookingRoomDto.getCheckOutDate());
        bookedRoom.setUser(userService.checkUser(bookingRoomDto.getUsername(), bookingRoomDto.getEmail()));
        bookedRoom.setRoom(roomService.findById(bookingRoomDto.getIdRoom()).get());
        bookedRoom.getRoom().addBooking();
        return bookedRoom;
    }

    @Override
    public boolean checkDate(LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate now = LocalDate.now();
        if (now.isAfter(checkInDate) || now.isAfter(checkOutDate)){
            return true;
        }
        if (checkInDate.isAfter(checkOutDate)){
            return true;
        }

        return false;
    }

    @Override
    public void edit(BookedRoom bookedRoom, Long id) {
        bookingRepository.save(bookedRoom);
    }

    @Override
    public void save(BookedRoom bookedRoom) {
        bookingRepository.save(bookedRoom);
    }

    @Override
    public void delete(Long id) {
        bookingRepository.findById(id).get().getRoom().cancelBooking();
        bookingRepository.deleteById(id);
    }
}
