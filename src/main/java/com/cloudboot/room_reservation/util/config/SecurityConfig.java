package com.cloudboot.room_reservation.util.config;
import com.cloudboot.room_reservation.member.repository.RefreshRepository;
import com.cloudboot.room_reservation.util.jwt.filter.CustomLogoutFilter;
import com.cloudboot.room_reservation.util.jwt.filter.JWTFilter;
import com.cloudboot.room_reservation.util.jwt.filter.LoginFilter;
import com.cloudboot.room_reservation.util.jwt.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 페이지
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/html/auth/**", "/html/main/**", "/api/join", "/api/login", "/reissue", "/api/logout").permitAll()

                        // 사용자
                        .requestMatchers(
                                "/html/dashboard/change-password.html",
                                "/html/dashboard/my-profile.html",
                                "/html/notice/notice.html",
                                "/html/reservation/reserve-list.html",
                                "/html/reservation/reserve.html"
                        ).hasRole("USER")

                        // 관리자
                        .requestMatchers(
                                "/html/dashboard/admin-dashboard.html",
                                "/html/dashboard/member-manage",
                                "/html/dashboard/user-dashboard",
                                "/html/notice/admin-notice.html",
                                "/html/reservation/admin-reservation"
                        ).hasRole("ADMIN")

                        // 사용자 API
                        .requestMatchers("/api/member/**", "/api/reservations/**", "/api/notice/**").hasRole("USER")

                        // 관리자 API
                        .requestMatchers("/api/admin/**", "/admin").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/html/notice/notice.html")
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/html/reservation/reserve.html")
                        )
                );



        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository, "/api/login"),
                        UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class) 0519 임시
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository, "/api/logout"), UsernamePasswordAuthenticationFilter.class);


        http
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://127.0.0.1:8080", "http://localhost:8080"));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Arrays.asList("access", "redirect-url", "Authorization"));

                        return configuration;
                    }
                }));

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}
