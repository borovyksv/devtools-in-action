package com.example.devtools.service;

import com.example.devtools.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {
    User save(User user);
    void delete(User user);
    User update(User user);
    List<User> getAllUsers();
    User findByEmail(String email);
    User findById(Long id);
}
