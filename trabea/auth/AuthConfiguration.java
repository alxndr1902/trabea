package com.bc46.trabea.auth;

import com.bc46.trabea.auth.handler.AuthAccessDeniedHandler;
import com.bc46.trabea.auth.handler.AuthEntryPoint;
import com.bc46.trabea.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class AuthConfiguration {
    private final AuthEntryPoint authEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthAccessDeniedHandler authAccessDeniedHandler, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        .requestMatchers(HttpMethod.GET,"/work-schedule", "/work-schedule/*").hasAnyRole("PARTTIMERS", "MANAGER")

                        // ADMINISTRATOR mapping
                        .requestMatchers(HttpMethod.GET, "/employee/part-time", "/employee/part-time/*").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/employee/part-time/register").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT,"/employee/part-time/*").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE,"/authors/*").hasRole("ADMINISTRATOR")

                        // PARTTIMER mapping
                        .requestMatchers(HttpMethod.POST,"/employee/part-time/*/work-schedule/*").hasRole("PARTTIMER")

                        // MANAGER mapping
                        .requestMatchers(HttpMethod.PATCH,"/work-schedule/*/approve").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH,"/work-schedule/*/dissaprove").hasRole("MANAGER")

                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(authAccessDeniedHandler));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("PUT", "POST", "GET", "DELETE", "PATCH"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
