package org.example.bookinghotel.service;

public interface GeneralService<T>{
    void save(T t);
    void delete(Long id);
}
