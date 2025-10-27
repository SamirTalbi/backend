package com.example.project.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // utilise le bean corsConfigurationSource() ci-dessous
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1) Autoriser le preflight CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 2) Routes publiques (docs/health/ping/racine)
                .requestMatchers("/", "/api/ping",
                                 "/actuator/health", "/actuator/info",
                                 "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // 3) Règles précises AVANT toute règle large
                .requestMatchers(HttpMethod.GET, "/api/ebooks/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ebooks/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/purchases/**").authenticated()

                // 4) Le reste de l’API nécessite un token
                .requestMatchers("/api/**").authenticated()

                // 5) Et tout le reste aussi
                .anyRequest().authenticated()
            );

        // Chaîner le filtre JWT
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS pour ton domaine front (Hostinger) + dev local
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of(
            "https://yramus.com",        // <- remplace par ton vrai domaine
            "http://localhost:4200"          // utile en dev local
        ));
        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setExposedHeaders(List.of("Authorization", "Content-Type"));
        // Mets true si tu utilises des cookies cross-site. Si JWT en header -> true/false indifférent.
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
