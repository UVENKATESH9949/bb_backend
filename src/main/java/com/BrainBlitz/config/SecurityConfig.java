package com.BrainBlitz.config;

import com.BrainBlitz.security.JwtFilter;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    // ═══════════════════════════════════════════════════════════
    // SECURITY FILTER CHAIN
    // ═══════════════════════════════════════════════════════════
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ── CORS: allow Next.js frontend ──────────────────
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ── CSRF: disabled — we use JWT, not sessions ─────
            .csrf(csrf -> csrf.disable())

            // ── Stateless sessions — JWT handles auth ─────────
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ── Route permissions ──────────────────────────────
            .authorizeHttpRequests(auth -> auth

                // Public auth routes — no token required
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/verify-otp",
                    "/api/auth/resend-otp",
                    "/api/auth/login",
                    "/api/auth/forgot-password",
                    "/api/auth/reset-password",
                    "/api/admin/login" ,         // admin login is also public
                    "/api/practice/**"
                ).permitAll()

                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // OPTIONS preflight — must be open for CORS to work
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // All other requests require a valid JWT token
                .anyRequest().authenticated()
            )

            // ── JWT filter — runs before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ═══════════════════════════════════════════════════════════
    // CORS — THE FIX
    // Previously localhost:3000 was not in allowed origins.
    // This is the root cause of "Network Error" on every API call
    // from the Next.js frontend.
    // ═══════════════════════════════════════════════════════════
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ── Allowed origins ───────────────────────────────────
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",    // Next.js dev server — THIS WAS MISSING
            "http://localhost:3001",    // fallback if 3000 is busy
            "http://127.0.0.1:3000" ,   // some browsers use 127.0.0.1
            "https://curly-orbit-r4rrjgqr74vp2wwp-3000.app.github.dev"
            // Add your production domain here later:
            // "https://brainblitz.in"
        ));

        // ── Allowed HTTP methods ──────────────────────────────
        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // ── Allowed headers ───────────────────────────────────
        config.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));

        // ── Expose Authorization header to frontend ───────────
        // Required so Axios interceptor can read it if needed
        config.setExposedHeaders(List.of("Authorization"));

        // ── Allow credentials (cookies / Authorization header)
        config.setAllowCredentials(true);

        // ── Preflight cache — browser won't re-send OPTIONS
        //    for 1 hour (3600 seconds)
        config.setMaxAge(3600L);

        // Apply to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ═══════════════════════════════════════════════════════════
    // BEANS
    // ═══════════════════════════════════════════════════════════

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
