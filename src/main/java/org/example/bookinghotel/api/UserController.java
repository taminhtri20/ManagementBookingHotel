package org.example.bookinghotel.api;

import jakarta.validation.Valid;
import org.example.bookinghotel.dto.BookingRoomDto;
import org.example.bookinghotel.model.BookedRoom;
import org.example.bookinghotel.model.Room;
import org.example.bookinghotel.model.User;
import org.example.bookinghotel.service.BookedRoomService;
import org.example.bookinghotel.service.PaginateRequest;
import org.example.bookinghotel.service.RoomService;
import org.example.bookinghotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/v1")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BookedRoomService bookedRoomService;
    @GetMapping("/rooms")
    public ResponseEntity<Page<Room>> findAllRoom(@PageableDefault(value = 6)Pageable pageable,
                                    @RequestParam(name = "roomType", required = false, defaultValue = "") String roomType,
                                    @RequestParam(name = "roomPrice", defaultValue = "10000000000",required = false) Long roomPrice,
                                    @RequestParam(name = "sortDir", required = false, defaultValue = "")String sortDir,
                                    @RequestParam(name = "sortField", required = false, defaultValue = "")String sortField){

        Page<Room> roomPage = roomService.findAll(new PaginateRequest(pageable.getPageNumber(), pageable.getPageSize()), roomType, roomPrice, sortField, sortDir);
        return new ResponseEntity<>(roomPage, HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<Page<BookedRoom>> findAllBooking(@PageableDefault(value = 5)Pageable pageable,
                                                     @RequestParam(name = "checkInDate", required = false, defaultValue = "") LocalDate checkInDate,
                                                     @RequestParam(name = "checkOutDate", required = false, defaultValue = "") LocalDate checkOutDate,
                                                     @RequestParam(name = "sortDir", required = false, defaultValue = "")String sortDir,
                                                     @RequestParam(name = "sortField", required = false, defaultValue = "")String sortField){
        if (checkInDate ==null || checkOutDate == null){
            checkInDate = LocalDate.now();
            checkOutDate = LocalDate.of(2024, Month.DECEMBER, 1);
        }
        Page<BookedRoom> bookedRooms = bookedRoomService.findAll(new PaginateRequest(pageable.getPageNumber(), pageable.getPageSize()), checkInDate, checkOutDate, sortField, sortDir);
        return new ResponseEntity<>(bookedRooms, HttpStatus.OK);
    }
    @GetMapping("/room/{id}")
    public ResponseEntity<Room> findRoom(@PathVariable Long id){
        Optional<Room> roomOptional = roomService.findById(id);
        if (!roomOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roomOptional.get(), HttpStatus.OK);
    }


    @GetMapping("/booking/{id}")
    public ResponseEntity<BookedRoom> findBooking(@PathVariable Long id){
        Optional<BookedRoom> bookedRoomOptional = bookedRoomService.findById(id);
        if (!bookedRoomOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookedRoomOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookedRoom> deleteBooking(@PathVariable Long id){
        Optional<BookedRoom> bookedRoomOptional = bookedRoomService.findById(id);
        if (!bookedRoomOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookedRoomService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<BookedRoom> saveBooking(@Valid @RequestBody BookingRoomDto bookingRoomDto , RedirectAttributes redirectAttributes) {
        BookedRoom bookedRoom = bookedRoomService.toBookedRoom(bookingRoomDto);
        Room room = roomService.findById(bookingRoomDto.getIdRoom()).get();
        if (bookedRoomService.
                checkDate(bookingRoomDto.getCheckInDate(), bookingRoomDto.getCheckOutDate())){
            redirectAttributes.addFlashAttribute("msg", "That bai");
            redirectAttributes.addFlashAttribute("status", "error");
        }else {
            redirectAttributes.addFlashAttribute("msg", "Thành công");
            bookedRoomService.save(bookedRoom);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
