package com.example.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.users.security.SecParams;

// Runs against the database in application.properties. It only reads the seeded accounts.
@SpringBootTest
@AutoConfigureMockMvc
class UsersProjApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Value("${jwt.secret}")
    private String secret;

    private ResultActions login(String username, String password) throws Exception {
        return mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"));
    }

    private String tokenFor(String username, String password) throws Exception {
        return login(username, password).andExpect(status().isOk())
                .andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    void loginReturnsASignedTokenWithTheRoles() throws Exception {
        DecodedJWT decoded = JWT.require(Algorithm.HMAC256(secret)).build()
                .verify(tokenFor("admin", "admin123"));

        assertThat(decoded.getSubject()).isEqualTo("admin");
        assertThat(decoded.getClaim("roles").asList(String.class)).contains("ADMIN", "USER");
        assertThat(decoded.getExpiresAt()).isAfter(new Date());
    }

    @Test
    void wrongPasswordIsRefused() throws Exception {
        login("admin", "wrong").andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("BAD_CREDENTIALS"));
    }

    @Test
    void allWithoutTokenIs401() throws Exception {
        mvc.perform(get("/all")).andExpect(status().isUnauthorized());
    }

    @Test
    void allWithInvalidTokenIs401() throws Exception {
        mvc.perform(get("/all").header("Authorization", "Bearer not-a-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void allWithUserRoleIs403() throws Exception {
        mvc.perform(get("/all").header("Authorization", SecParams.PREFIX + tokenFor("yasser", "yasser123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void allWithAdminRoleWorksAndHidesPasswords() throws Exception {
        mvc.perform(get("/all").header("Authorization", SecParams.PREFIX + tokenFor("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").exists())
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }
}
