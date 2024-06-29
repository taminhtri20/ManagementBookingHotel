package org.example.bookinghotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingHotelApplication {

    public static void main(String[] args) {

        SpringApplication.run(BookingHotelApplication.class, args);
        System.out.println("http://localhost:8080/admin");
    }

}
