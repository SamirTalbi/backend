package com.example.project.security;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // Active la configuration CORS ci-dessous
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Préflight CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Routes publiques
                .requestMatchers("/", "/api/ping", "/actuator/health", "/actuator/info",
                                 "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // Ebooks : lecture publique, écriture admin
                .requestMatchers(HttpMethod.GET, "/api/ebooks/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ebooks/**").hasRole("ADMIN")

                // Achats : nécessite connexion
                .requestMatchers(HttpMethod.POST, "/api/purchases/**").authenticated()

                // Tout le reste nécessite un token JWT
                .anyRequest().authenticated()
            );

        // Filtre JWT avant le filtre d'authentification standard
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuration CORS pour autoriser ton front-end Angular à accéder au backend Render.
     * Ajoute ici tous les domaines de ton front (prod, dev, test).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of(
            "https://yramus.com",               // ton domaine principal (Hostinger)
            "https://tonfront.onrender.com",    // ton front s’il est sur Render
            "http://localhost:4200"             // pour les tests Angular locaux
        ));
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true); // true si tu utilises JWT dans le header Authorization

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
