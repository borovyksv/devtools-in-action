package com.example.devtools;

import com.example.devtools.domain.User;
import com.example.devtools.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    UsersService usersService;

    @GetMapping("/users")
    public List<User> getUsers(){
        return usersService.getAllUsers();
    }
}
