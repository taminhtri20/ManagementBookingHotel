package org.example.bookinghotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.bookinghotel.model.BookedRoom;
import org.example.bookinghotel.model.Role;
import org.example.bookinghotel.model.Room;
import org.example.bookinghotel.model.User;
import org.example.bookinghotel.repository.UserRepository;
import org.example.bookinghotel.service.RoomService;
import org.example.bookinghotel.service.UserService;
import org.example.bookinghotel.service.impl.RoomServiceImpl;
import org.example.bookinghotel.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
public class BookingRoomDto {

    @NotNull(message = "Check in pls!!!")
    private LocalDate checkInDate;
    @NotNull(message = "Check out pls!!!")
    private LocalDate checkOutDate;
    @NotBlank(message = "Check username pls!!!")
    private String username;
    @NotBlank(message = "Check email pls!!!")
    private String email;
    private Long idRoom;

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public BookingRoomDto setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public BookingRoomDto setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public BookingRoomDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BookingRoomDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public Long getIdRoom() {
        return idRoom;
    }
    public BookingRoomDto setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
        return this;
    }
}
