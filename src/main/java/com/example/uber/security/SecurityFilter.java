package com.example.uber.security;

import com.example.uber.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityFilter {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * This code defines a Spring Bean that customizes web security settings.
     * - A WebSecurityCustomizer is created to customize web security.
     * - It configures the application to ignore security for requests matching "/api/auth/**".
     * - This allows these specific requests to bypass security checks.
     */

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/api/auth/**","/api/permitAll/**"
        );
    }


    /**
     * This code configures a SecurityFilterChain as a Spring Bean.
     * - SecurityFilterChain defines the security configuration for HTTP requests.
     * - It disables Cross-Site Request Forgery (CSRF) protection.
     * - Specifies access control rules for URL patterns, allowing public access to "/api/user/auth/**" and
     *   requiring authentication for other requests.
     * - Configures stateless session management for the application.
     * - Specifies an AuthenticationProvider for user authentication.
     * - Adds a custom JWT authentication filter before the standard UsernamePasswordAuthenticationFilter.
     * - The configured SecurityFilterChain is returned as a Spring Bean.
     * - Exception handling is included to handle configuration errors gracefully.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        try {
            http
                    .cors((cors) -> {
                        cors.configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.applyPermitDefaultValues();
                            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Add allowed methods
                            config.addAllowedOrigin("http://localhost:3000"); // Specify your frontend URL
                            return config;
                        });
                    })
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((req) -> req
                            .requestMatchers("/api/permitAll/**").permitAll()
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                            .requestMatchers("/api/user/**").hasAuthority("USER")
                            .anyRequest().authenticated()
                    )
                    .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter,
                            UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling((exception) -> exception.accessDeniedHandler(customAccessDeniedHandler));


            return http.build();
        } catch (Exception e) {
            // Handle exceptions gracefully, e.g., log or throw a custom exception
            throw new RuntimeException("Error configuring security filter chain", e);
        }

    }

    /**
     * This code configures a Spring Bean for an AuthenticationProvider.
     * - AuthenticationProvider handles user authentication.
     * - A DaoAuthenticationProvider is created and configured.
     * - It's linked to a user service for fetching user details and a BCryptPasswordEncoder for secure password handling.
     * - The configured AuthenticationProvider is returned as a Spring Bean for authentication use.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authenticationService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationProvider;
    }

    /**
     * This code defines a Spring Bean for configuring and providing an AuthenticationManager.
     * - AuthenticationManager manages authentication operations in a Spring Security-enabled application.
     * - It takes an `AuthenticationConfiguration` object for configuration.
     * - The method retrieves a pre-configured AuthenticationManager from the `config` object.
     * - It may throw exceptions for configuration issues.
     * - The configured AuthenticationManager is returned as a Spring Bean for use throughout the application.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
