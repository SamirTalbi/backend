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
            .cors(cors -> {}) // active CORS via le bean corsConfigurationSource()
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 🔹 Préflight CORS (OPTIONS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔹 Endpoints publics généraux
                .requestMatchers(
                    "/", "/api/ping",
                    "/actuator/health", "/actuator/info",
                    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
                ).permitAll()

                // 🔹 Authentification (inscription & connexion)
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                // 🔹 Lecture publique des ebooks
                .requestMatchers(HttpMethod.GET, "/api/ebooks/**").permitAll()

                // 🔹 Gestion des ebooks (ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ebooks/**").hasRole("ADMIN")

                // 🔹 Achats (nécessitent une authentification JWT)
                .requestMatchers(HttpMethod.POST, "/api/purchases/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/purchases/**").authenticated()

                // 🔹 Tout le reste temporairement ouvert (tu pourras le restreindre ensuite)
                .anyRequest().permitAll()
            );

        // 🔹 Ajout du filtre JWT avant UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of(
            "https://yramus.com",       // domaine principal du front Hostinger
            "https://www.yramus.com"    // (optionnel, pour le sous-domaine www)
        ));
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true); // autorise l'envoi du header Authorization (Bearer ...)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
