package org.hamlet.blogwebsite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
                                 // Permit all for authentication endpoints and public blog post access
                                .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/auth/**"),
                                        antMatcher(HttpMethod.GET, "/api/v1/**")).permitAll()
                                // Permit all for Swagger UI and API documentation
                                .requestMatchers(antMatcher(HttpMethod.GET, "/swagger-ui.html"),
                                        antMatcher(HttpMethod.GET, "/swagger-ui/**"),
                                        antMatcher(HttpMethod.GET, "/v3/api-docs/**"),
                                        antMatcher(HttpMethod.GET, "/swagger-resources/**")).permitAll()
                                // Open access for user registration
                                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                                // Restrict access for blog post management
                                .requestMatchers("/api/v1/author-requests/approve/**", "/api/v1/author-requests/reject/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/author-requests/request").hasRole("REGISTERED_USER")
                                .requestMatchers("/api/v1/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                                .anyRequest().authenticated()

                )
                .exceptionHandling(exception-> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
        http.authenticationProvider(authenticationProvider);
        return http.build();
    }
}
