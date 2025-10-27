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
            .cors(cors -> {}) // active CORS (voir bean corsConfigurationSource() plus bas)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Laisse passer les préflights CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Endpoints publics (pour la santé et un ping)
                .requestMatchers("/", "/api/ping", "/actuator/health", "/actuator/info",
                                 "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // Ebooks : lecture publique (GET), écriture réservée ADMIN
                .requestMatchers(HttpMethod.GET, "/api/ebooks/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ebooks/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ebooks/**").hasRole("ADMIN")

                // Achats : nécessitent une authentification
                .requestMatchers(HttpMethod.POST, "/api/purchases/**").authenticated()

                // Tout le reste → authentifié
                .anyRequest().authenticated()
            );

        // Le filtre JWT avant UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ⚠️ Remplace les domaines ci-dessous par ton (ou tes) vrai(s) domaine(s) Hostinger
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of(
            "https://yramus.com",      // ← mets ton domaine front ici
            "https://ton-sous-domaine.host"    // ← et/ou ici, ou supprime
        ));
        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        // Mets true seulement si tu utilises des cookies (sessions). En JWT header Bearer -> false possible.
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
