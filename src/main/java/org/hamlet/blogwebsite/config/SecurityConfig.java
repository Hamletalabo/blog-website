package org.hamlet.blogwebsite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection as we are using JWT
                .csrf(CsrfConfigurer::disable)

                // Set up exception handling for unauthorized access
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // Manage sessions statelessly since we are using JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define authorization rules
                .authorizeHttpRequests(requests -> requests
                        // Public access to registration and authentication endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/**").permitAll()

                        // Public access to blog posts (GET requests)
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()

                        // Public access to Swagger and API docs
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                        // Require authentication for creating, updating, and deleting posts
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasAuthority("USER")

                        // Require authentication for user-specific endpoints
                        .requestMatchers("/api/v1/user/**").hasAuthority("USER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                );

        // Add the JWT authentication filter before the default authentication filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Set the custom authentication provider
        http.authenticationProvider(authenticationProvider);

        return http.build();
    }

    // Define CORS filter separately
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Adjust according to your frontend URL
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
