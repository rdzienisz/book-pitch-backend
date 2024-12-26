package com.wsb.book_pitch;

import com.wsb.book_pitch.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final Integer AUTHORIZATION_HEADER_INDEX = 7;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the current request URI
        String requestUri = request.getRequestURI();

        // Skip JWT processing for login and other public endpoints
        if (requestUri.equals("/api/auth/login") || requestUri.startsWith("/api/user/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Proceed with JWT processing for other endpoints
        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(AUTHORIZATION_HEADER_INDEX);
            LOG.debug("Extracted token: {}", token);
            try {
                Claims claims = JwtUtil.extractClaims(token);
                LOG.debug("Extracted claims: {}", claims);
                if (claims != null && !JwtUtil.isTokenExpired(token)) {
                    LOG.info("Token is valid, setting authentication context for user: {}", claims.getSubject());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    LOG.warn("Token is expired or invalid");
                }
            } catch (Exception e) {
                LOG.error("JWT token processing failed", e);
            }
        } else {
            LOG.warn("Authorization header is missing or does not start with Bearer");
        }


        filterChain.doFilter(request, response);
    }
}