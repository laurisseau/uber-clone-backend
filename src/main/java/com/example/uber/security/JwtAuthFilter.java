package com.example.uber.security;

import com.example.uber.service.DriverAuthenticationService;
import com.example.uber.service.UserAuthenticationService;
import com.example.uber.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationService userAuthenticationService;
    private final DriverAuthenticationService driverAuthenticationService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        @NonNull final String authHeader = request.getHeader("Authorization");
        @NonNull final String jwtToken;
        @NonNull final String username;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);

        username = jwtUtils.extractUsername(jwtToken);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            boolean checkUser = userAuthenticationService.jwtAuthCheck(username);

            if(!checkUser){
                UserDetails driverDetails = driverAuthenticationService.loadUserByUsername(username);
                if (driverDetails != null && jwtUtils.isTokenValid(jwtToken, driverDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(driverDetails, null, driverDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else {
                UserDetails userDetails = userAuthenticationService.loadUserByUsername(username);

                if (userDetails != null && jwtUtils.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER"))) {

            authorities.add(new SimpleGrantedAuthority("USER"));

        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {

            authorities.add(new SimpleGrantedAuthority("ADMIN"));

        }else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("DRIVER"))) {

            authorities.add(new SimpleGrantedAuthority("DRIVER"));

        }


        filterChain.doFilter(request,response);

    }
}
