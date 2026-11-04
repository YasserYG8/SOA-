package com.example.users.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.users.entities.User;
import com.example.users.service.UserService;

// Lets Spring Security read users from our own table.
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> (GrantedAuthority) new SimpleGrantedAuthority(r.getRole()))
                .toList();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Boolean.TRUE.equals(user.getEnabled()),
                true, true, true, authorities);
    }
}
