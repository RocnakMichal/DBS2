package cz.uhk.dbsproject.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF Configuration
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login", "/register", "/logout", "groups/create", "movies/add", "users/add", "profile/edit","movie/edit", "/change-password","/movies/edit") // Disable CSRF for specified endpoints
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
