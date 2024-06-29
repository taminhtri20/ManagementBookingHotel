package org.example.bookinghotel.service;


import org.example.bookinghotel.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService extends GeneralService<User> {
    Page<User> findAll(PaginateRequest paginateRequest ,String username, String email, String gender, String numberPhone, String sortField , String sort);
    Page<User> findAll(Pageable pageable);
    User checkUser(String username, String email);
    Optional<User> findById(Long id);
}
