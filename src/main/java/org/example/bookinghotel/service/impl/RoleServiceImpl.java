package org.example.bookinghotel.service.impl;

import org.example.bookinghotel.model.Role;
import org.example.bookinghotel.model.Room;
import org.example.bookinghotel.repository.RoleRepository;
import org.example.bookinghotel.service.RoleService;
import org.example.bookinghotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
