package org.example.bookinghotel.service.impl;

import org.example.bookinghotel.model.Role;
import org.example.bookinghotel.model.User;
import org.example.bookinghotel.repository.UserRepository;
import org.example.bookinghotel.service.PaginateRequest;
import org.example.bookinghotel.service.SearchCriteria;
import org.example.bookinghotel.service.UserService;
import org.example.bookinghotel.service.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findAll(PaginateRequest paginateRequest , String username, String email, String gender, String numberPhone, String sortField, String sort) {
        Pageable paging = null;
        if (sort.isEmpty() &&sortField.isEmpty()){
            paging = PageRequest.of(paginateRequest.getPage(),  paginateRequest.getSize());
        }else {
            Sort sort1 = sort.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
            paging = PageRequest.of(paginateRequest.getPage(),  paginateRequest.getSize(), sort1);
        }
        UserSpec userSpec = new UserSpec(new SearchCriteria("username", ":", username));
        UserSpec userSpec1 = new UserSpec(new SearchCriteria("email", ":", email));
        UserSpec userSpec2 = new UserSpec(new SearchCriteria("gender", ":", gender));
        UserSpec userSpec3 = new UserSpec(new SearchCriteria("numberPhone", ":", numberPhone));
        Page<User> userPage = userRepository.findAll(Specification.where(userSpec).
                and(Specification.where(userSpec1)).
                and(Specification.where(userSpec2)).
                and(Specification.where(userSpec3)), paging);
        return userPage;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User checkUser(String username, String email) {
        User user1 = new User();
        for (User user : userRepository.findAll()){
            if (user.getUsername().equals(username) && user.getEmail().equals(email)){
                user1 = user;
                return user1;
            }
        }
        user1.setUsername(username);
        user1.setEmail(email);
        user1.setPassword("123456");
        Role role = new Role();
        user1.setRole(role);
        user1.getRole().setId(2);
        user1.setGender("");
        user1.setNumberPhone("");
        userRepository.save(user1);
        return user1;
    }


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
