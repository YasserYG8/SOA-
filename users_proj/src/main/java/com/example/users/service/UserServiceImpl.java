package com.example.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.users.entities.Role;
import com.example.users.entities.User;
import com.example.users.repos.RoleRepository;
import com.example.users.repos.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findRole(String role) {
        return roleRepository.findByRole(role);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        User user = userRepository.findByUsername(username);
        user.getRoles().add(roleRepository.findByRole(rolename));
        return user;
    }
}
