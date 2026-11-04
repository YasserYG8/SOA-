package com.example.users.security;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Handles POST /login: checks the credentials, then issues the token.
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LoginRequest(String username, String password) {
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String secret;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        super(authenticationManager);
        this.secret = secret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest login = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(login.username(), login.password()));
        } catch (IOException e) {
            throw new BadCredentialsException("Malformed login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        String[] roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);

        String jwt = JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecParams.EXP_TIME))
                .sign(Algorithm.HMAC256(secret));

        response.addHeader("Authorization", jwt);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {
        boolean disabled = failed instanceof DisabledException;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Map.of(
                "errorCode", disabled ? "USER_DISABLED" : "BAD_CREDENTIALS",
                "message", disabled ? "This account is disabled. Verify your email first."
                        : "Wrong username or password."));
    }
}
