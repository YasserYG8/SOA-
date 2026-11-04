package com.example.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.users.entities.User;
import com.example.users.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteByUser(User user);
}
