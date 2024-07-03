package com.ecomerce.auth.config;

import com.ecomerce.auth.service.UsersDetailService;
import com.ecomerce.auth.token.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsersDetailService usersDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String email = jwtService.extractEmail(jwt);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
          UserDetails userDetails = usersDetailService.loadUserByUsername(email);
          if(userDetails != null && jwtService.isTokenValid(jwt)){
              UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                      email,
                      userDetails.getPassword(),
                      userDetails.getAuthorities()
              );
              authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }
        filterChain.doFilter(request, response);
    }
}
