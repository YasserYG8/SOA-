package com.example.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.example.users.entities.User;
import com.example.users.entities.VerificationToken;
import com.example.users.repos.UserRepository;
import com.example.users.repos.VerificationTokenRepository;

// @Transactional rolls back each test, and the blank mail username means no real email is sent.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = "spring.mail.username=")
class RegistrationTests {

    private static final String USERNAME = "new_driver";
    private static final String EMAIL = "new_driver@example.com";
    private static final String PASSWORD = "secret123";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    private ResultActions register(String username, String email, String password) throws Exception {
        return mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"));
    }

    private ResultActions login(String password) throws Exception {
        return mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + USERNAME + "\",\"password\":\"" + password + "\"}"));
    }

    private VerificationToken tokenOf(String username) {
        User user = userRepository.findByUsername(username);
        return tokenRepository.findAll().stream().filter(t -> t.getUser().equals(user)).findFirst().orElseThrow();
    }

    @Test
    void newAccountIsDisabledUntilTheEmailIsVerified() throws Exception {
        register(USERNAME, EMAIL, PASSWORD).andExpect(status().isCreated())
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.roles[0].role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
        assertThat(userRepository.findByUsername(USERNAME).getPassword()).isNotEqualTo(PASSWORD);

        login(PASSWORD).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("USER_DISABLED"));

        String code = tokenOf(USERNAME).getToken();
        assertThat(code).matches("\\d{6}");
        mvc.perform(get("/verifyEmail/" + code)).andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));

        login(PASSWORD).andExpect(status().isOk());
        // The code works only once.
        mvc.perform(get("/verifyEmail/" + code)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_TOKEN"));
    }

    @Test
    void unknownCodeIsRefused() throws Exception {
        mvc.perform(get("/verifyEmail/abc")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_TOKEN"));
    }

    @Test
    void expiredCodeIsRefusedAndRegisteringAgainGivesANewOne() throws Exception {
        register(USERNAME, EMAIL, PASSWORD).andExpect(status().isCreated());
        VerificationToken token = tokenOf(USERNAME);
        token.setExpirationTime(Instant.now().minusSeconds(1));
        String oldCode = token.getToken();

        mvc.perform(get("/verifyEmail/" + oldCode)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("EXPIRED_TOKEN"));

        register(USERNAME, EMAIL, PASSWORD).andExpect(status().isCreated());
        assertThat(tokenRepository.findByToken(oldCode)).isNull();
        mvc.perform(get("/verifyEmail/" + tokenOf(USERNAME).getToken())).andExpect(status().isOk());
    }

    @Test
    void takenUsernameOrEmailIsRefused() throws Exception {
        register("admin", EMAIL, PASSWORD).andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("USERNAME_ALREADY_EXISTS"));

        register(USERNAME, EMAIL, PASSWORD).andExpect(status().isCreated());
        mvc.perform(get("/verifyEmail/" + tokenOf(USERNAME).getToken())).andExpect(status().isOk());
        register("someone_else", EMAIL, PASSWORD).andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("EMAIL_ALREADY_EXISTS"));
    }

    @Test
    void invalidInputIsRefused() throws Exception {
        register("ab", EMAIL, PASSWORD).andExpect(status().isBadRequest());
        register(USERNAME, "not-an-email", PASSWORD).andExpect(status().isBadRequest());
        register(USERNAME, EMAIL, "short").andExpect(status().isBadRequest());
    }

    @Test
    void aStaleTokenDoesNotBlockRegistration() throws Exception {
        mvc.perform(post("/register").header("Authorization", "Bearer null").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + USERNAME + "\",\"email\":\"" + EMAIL + "\",\"password\":\"" + PASSWORD + "\"}"))
                .andExpect(status().isCreated());
    }
}
