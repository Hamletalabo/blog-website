package org.hamlet.blogwebsite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .csrf(CsrfConfigurer::disable)
//                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**",
//                                "/api/v1/auth/password-reset",
//                                "/token/**")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/**",
//                                "/swagger-ui.html",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-resources/**")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/blog/**")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/v1/blog/**").hasAnyAuthority("ROLE_USERS")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/blog/**").hasAnyAuthority("ROLE_USERS")
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .httpBasic(Customizer.withDefaults());
//
//        http.authenticationProvider(authenticationProvider);
//        return http.build();
//    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**",
                                "/api/v1/auth/password-reset",
                                "/token/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/blog/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/blog/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/blog/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.authenticationProvider(authenticationProvider);
        return http.build();
    }
}
