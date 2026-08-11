package com.hero.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Routes publiques (client sans authentification)
                .requestMatchers(HttpMethod.GET, "/api/produits").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/produits/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/panier/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/panier/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/panier/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/commandes/checkout").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/commandes/client/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/commandes/client/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/commandes/*/payer").permitAll()

                // Routes Admin uniquement
                .requestMatchers(HttpMethod.POST, "/api/produits").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/produits/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/produits/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/produits/*/promo").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/produits/*/promo").hasRole("ADMIN")
                .requestMatchers("/api/clients/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/commandes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/commandes/*/statut").hasRole("ADMIN")

                // Tout le reste est authentifié
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}