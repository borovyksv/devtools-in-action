package com.example.devtools.service;

import com.example.devtools.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {
    public List<User> getAllUsers();
}
