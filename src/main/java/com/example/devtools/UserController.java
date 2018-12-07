package com.example.devtools;

import com.example.devtools.domain.User;
import com.example.devtools.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return usersService.save(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody User user) {
        usersService.delete(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return usersService.update(user);
    }

    @GetMapping
    public List<User> getUsers(){
        return usersService.getAllUsers();
    }
}
