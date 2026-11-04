package com.example.users.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.users.dto.RegistrationRequest;
import com.example.users.entities.Role;
import com.example.users.entities.User;
import com.example.users.entities.VerificationToken;
import com.example.users.exceptions.EmailAlreadyExistsException;
import com.example.users.exceptions.ExpiredTokenException;
import com.example.users.exceptions.InvalidTokenException;
import com.example.users.exceptions.UsernameAlreadyExistsException;
import com.example.users.repos.RoleRepository;
import com.example.users.repos.UserRepository;
import com.example.users.repos.VerificationTokenRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Duration CODE_LIFETIME = Duration.ofMinutes(15);
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

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

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        User byEmail = userRepository.findByEmail(request.email());
        if (byEmail != null) {
            if (Boolean.TRUE.equals(byEmail.getEnabled())) {
                throw new EmailAlreadyExistsException();
            }
            removeUnverifiedUser(byEmail);
        }
        User byUsername = userRepository.findByUsername(request.username());
        if (byUsername != null) {
            if (Boolean.TRUE.equals(byUsername.getEnabled())) {
                throw new UsernameAlreadyExistsException();
            }
            removeUnverifiedUser(byUsername);
        }

        User user = new User(request.username(), passwordEncoder.encode(request.password()), false);
        user.setEmail(request.email());
        user.getRoles().add(roleRepository.findByRole("USER"));
        user = userRepository.save(user);

        String code = generateCode();
        tokenRepository.save(new VerificationToken(code, user, Instant.now().plus(CODE_LIFETIME)));
        emailService.sendVerificationCode(user.getEmail(), code); // a failure here rolls the registration back
        return user;
    }

    @Override
    public User verifyEmail(String code) {
        VerificationToken token = tokenRepository.findByToken(code);
        if (token == null) {
            throw new InvalidTokenException();
        }
        if (token.getExpirationTime().isBefore(Instant.now())) {
            throw new ExpiredTokenException();
        }
        User user = token.getUser();
        user.setEnabled(true);
        tokenRepository.delete(token);
        return user;
    }

    // An account that never verified its email can register again, for example after its code expired.
    private void removeUnverifiedUser(User user) {
        tokenRepository.deleteByUser(user);
        userRepository.delete(user);
        userRepository.flush();
    }

    private String generateCode() {
        String code;
        do {
            code = String.format("%06d", RANDOM.nextInt(1_000_000));
        } while (tokenRepository.findByToken(code) != null);
        return code;
    }
}
