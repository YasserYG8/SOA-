package com.example.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.users.entities.Role;
import com.example.users.entities.User;
import com.example.users.service.UserService;

@SpringBootApplication
public class UsersProjApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(UsersProjApplication.class, args);
    }

    // Demo data for local development: two accounts, one per role.
    @Override
    public void run(String... args) {
        if (userService.findRole("ADMIN") == null) {
            userService.addRole(new Role("ADMIN"));
        }
        if (userService.findRole("USER") == null) {
            userService.addRole(new Role("USER"));
        }
        if (userService.findUserByUsername("admin") == null) {
            userService.saveUser(new User("admin", "admin123", true));
            userService.addRoleToUser("admin", "ADMIN");
            userService.addRoleToUser("admin", "USER");
        }
        if (userService.findUserByUsername("yasser") == null) {
            userService.saveUser(new User("yasser", "yasser123", true));
            userService.addRoleToUser("yasser", "USER");
        }
    }
}
