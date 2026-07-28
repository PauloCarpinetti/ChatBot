package br.com.paulo.chatbot.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String tenantIdStr = decodedJWT.getClaim("tenantId").asString();
            if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
                UUID tenantId = UUID.fromString(tenantIdStr);
                TenantAuthenticationToken authentication = new TenantAuthenticationToken(tenantId);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JWTVerificationException | IllegalArgumentException e) {
            // Token is invalid, expired, or tenantId is not a valid UUID.
            // SecurityContext remains clear, and Spring Security will return 401 on protected routes.
        }

        filterChain.doFilter(request, response);
    }
}
