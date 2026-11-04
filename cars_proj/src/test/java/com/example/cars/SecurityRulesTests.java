package com.example.cars;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cars.security.SecParams;

// @Transactional rolls back every write, so these tests leave the database unchanged.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityRulesTests {

    private static final String NEW_CAR = "{\"model\":\"Test\",\"price\":1.0,\"brand\":{\"idBrand\":1}}";
    private static final String NEW_BRAND = "{\"name\":\"Test\"}";

    @Autowired
    private MockMvc mvc;

    @Value("${jwt.secret}")
    private String secret;

    private String bearer(String key, long lifetimeMs, String... roles) {
        return SecParams.PREFIX + JWT.create()
                .withSubject("test")
                .withArrayClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + lifetimeMs))
                .sign(Algorithm.HMAC256(key));
    }

    private String admin() {
        return bearer(secret, 60_000, "ADMIN", "USER");
    }

    private String user() {
        return bearer(secret, 60_000, "USER");
    }

    @Test
    void noTokenIs401() throws Exception {
        mvc.perform(get("/api/all")).andExpect(status().isUnauthorized());
        mvc.perform(get("/brand")).andExpect(status().isUnauthorized());
    }

    @Test
    void forgedTokenIs401() throws Exception {
        String forged = bearer("a-different-secret-key-of-enough-length", 60_000, "ADMIN");
        mvc.perform(get("/api/all").header("Authorization", forged)).andExpect(status().isUnauthorized());
    }

    @Test
    void expiredTokenIs401() throws Exception {
        String expired = bearer(secret, -60_000, "ADMIN");
        mvc.perform(get("/api/all").header("Authorization", expired)).andExpect(status().isUnauthorized());
    }

    @Test
    void userCanRead() throws Exception {
        for (String url : new String[] { "/api/all", "/api/1", "/api/brands", "/api/brands/1", "/api/carsbrand/1",
                "/api/carsbymodel/GT", "/brand", "/brand/1" }) {
            mvc.perform(get(url).header("Authorization", user())).andExpect(status().isOk());
        }
    }

    @Test
    void userCannotWrite() throws Exception {
        mvc.perform(post("/api").header("Authorization", user()).contentType(MediaType.APPLICATION_JSON).content(NEW_CAR))
                .andExpect(status().isForbidden());
        mvc.perform(put("/api").header("Authorization", user()).contentType(MediaType.APPLICATION_JSON).content(NEW_CAR))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/api/1").header("Authorization", user())).andExpect(status().isForbidden());
        mvc.perform(post("/brand").header("Authorization", user()).contentType(MediaType.APPLICATION_JSON).content(NEW_BRAND))
                .andExpect(status().isForbidden());
        mvc.perform(put("/brand/1").header("Authorization", user()).contentType(MediaType.APPLICATION_JSON).content(NEW_BRAND))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/brand/1").header("Authorization", user())).andExpect(status().isForbidden());
    }

    @Test
    void adminCanWrite() throws Exception {
        mvc.perform(post("/api").header("Authorization", admin()).contentType(MediaType.APPLICATION_JSON).content(NEW_CAR))
                .andExpect(status().isOk());
        mvc.perform(post("/brand").header("Authorization", admin()).contentType(MediaType.APPLICATION_JSON).content(NEW_BRAND))
                .andExpect(status().isCreated());
    }

    @Test
    void carRepositoryIsNotPublished() throws Exception {
        mvc.perform(get("/cars").header("Authorization", admin())).andExpect(status().isNotFound());
    }

    @Test
    void preflightFromAngularIsAllowedWithoutToken() throws Exception {
        mvc.perform(options("/api/1").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "DELETE"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    void otherOriginsAreRefused() throws Exception {
        mvc.perform(options("/api/1").header("Origin", "http://evil.example")
                .header("Access-Control-Request-Method", "DELETE"))
                .andExpect(status().isForbidden());
    }
}
