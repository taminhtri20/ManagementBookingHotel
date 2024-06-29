package org.example.bookinghotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @NotBlank(message = "Vui long nhap kieu phong")
    private String roomType;
    @NotNull(message = "Vui long nhap gia tien")
    @Min(value = 0, message = "Vui long nhap gia tien > 0")
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    private String description;
    private String directionImage;
    public void addBooking(){
        isBooked = true;
    }
    public void cancelBooking(){
        isBooked = false;
    }
}