package com.example.cars.security;

import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// This API never creates tokens. It trusts the ones signed by users_proj with the shared key.
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(SecParams.PREFIX)) {
            try {
                DecodedJWT decoded = JWT.require(Algorithm.HMAC256(SecParams.SECRET)).build()
                        .verify(header.substring(SecParams.PREFIX.length())); // checks the signature + expiry
                List<String> roles = decoded.getClaim("roles").asList(String.class);
                List<GrantedAuthority> authorities = (roles == null ? List.<String>of() : roles).stream()
                        .map(r -> (GrantedAuthority) new SimpleGrantedAuthority(r))
                        .toList();
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(decoded.getSubject(), null, authorities));
            } catch (JWTVerificationException e) {
                // Stay anonymous, so protected routes answer 401.
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
