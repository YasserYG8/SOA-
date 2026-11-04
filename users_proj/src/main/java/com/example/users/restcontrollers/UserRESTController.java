package com.example.users.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.users.dto.RegistrationRequest;
import com.example.users.entities.User;
import com.example.users.service.UserService;
import jakarta.validation.Valid;

@RestController
public class UserRESTController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@Valid @RequestBody RegistrationRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/verifyEmail/{code}")
    public User verifyEmail(@PathVariable("code") String code) {
        return userService.verifyEmail(code);
    }
}
