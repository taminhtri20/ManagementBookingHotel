package org.example.bookinghotel.service.impl;

import org.example.bookinghotel.model.Room;
import org.example.bookinghotel.model.User;
import org.example.bookinghotel.repository.RoomRepository;
import org.example.bookinghotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomRepository roomRepository;
    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Page<Room> findAll(PaginateRequest paginateRequest ,String roomType, Long roomPrice, String sortField , String sort) {
        Pageable paging = null;
        BigDecimal maxRoomPrice = BigDecimal.valueOf(roomPrice);
        if (sort.isEmpty()&& sortField.isEmpty()){
            paging = PageRequest.of(paginateRequest.getPage(),  paginateRequest.getSize());
        }else {
            Sort sort1 = sort.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
            paging = PageRequest.of(paginateRequest.getPage(),  paginateRequest.getSize(), sort1);
        }
        RoomSpec roomSpec = new RoomSpec(new SearchCriteria("roomType", ":", roomType));
        RoomSpec roomSpec1 = new RoomSpec(new SearchCriteria("roomPrice", "<", maxRoomPrice
        ));
        Page<Room> roomPage = roomRepository.findAll(Specification.where(roomSpec).
                and(Specification.where(roomSpec1)), paging);
        return roomPage;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    @Override
    public Room checkRoom(Room room) {
        return null;
    }
}
