package com.tech.filter;

import com.tech.model.User;
import com.tech.repo.UserRepository;
import com.tech.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        // Bearer token is not provided
        if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer token is present in the header
        String userEmail;
        try {
            String token = authorizationHeader.split(" ")[1];
            Claims claims = JwtUtils.parseJWT(token);
            userEmail = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token invalid");
        }

        Optional<User> user = userRepository.findByEmail(userEmail);

        // store user info in SecurityContextHolder (this will authenticate the user)
        UsernamePasswordAuthenticationToken authToken = user.map(u -> new UsernamePasswordAuthenticationToken(u, null, null)).orElseThrow(() -> new RuntimeException("User not found, invalid token"));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);

    }
}
