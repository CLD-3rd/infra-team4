package com.cloudboot.room_reservation.util.jwt.filter;


import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.request.LoginRequest;
import com.cloudboot.room_reservation.member.entity.RefreshToken;
import com.cloudboot.room_reservation.member.repository.RefreshRepository;
import com.cloudboot.room_reservation.util.exception.ErrorResult;
import com.cloudboot.room_reservation.util.jwt.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository, String loginUrl) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.setFilterProcessesUrl(loginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            log.info("username = {}", username);
            log.info("password = {}", password);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();

        Long id = memberDetails.getId();
        String username = memberDetails.getUsername();
        String role = getRole(memberDetails);

        String access = jwtUtil.createJwt("access", id, username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", id, username, role, 86400000L);

        addRefreshEntity(username, refresh, 86400000L);

        String redirectUrl = findRedirectUrl(role);

        response.setHeader("access", access);
        response.setHeader("redirect-url", redirectUrl);
        response.addCookie(createCookie("refresh", refresh));
        response.addCookie(createCookie("access", access));
        response.setStatus(HttpStatus.OK.value());

    }

    private String findRedirectUrl(String role) {
        log.info("LOGIN ROLE = {}", role);
        if (role.equals("ROLE_USER")) {
            return "user-dashboard.html";
        }
        else {
            return "admin-dashboard.html";
        }
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken refreshEntity = RefreshToken.of(username, refresh, date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setDomain("127.0.0.1");
        cookie.setHttpOnly(true);

        return cookie;
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ErrorResult errorResult;

        // 예외 메시지에 따라 분기
        String message = failed.getMessage();

        if ("존재하지 않는 아이디입니다.".equals(message)) {
            errorResult = new ErrorResult("UsernameNotFound", message);
        } else if ("비밀번호가 일치하지 않습니다.".equals(message)) {
            errorResult = new ErrorResult("PasswordMismatch", message);
        } else {
            errorResult = new ErrorResult("LoginFailed", "로그인 실패: 알 수 없는 오류");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(result);
    }

    private static String getRole(CustomMemberDetails memberDetails) {
        Collection<? extends GrantedAuthority> authorities = memberDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        return auth.getAuthority();
    }
}
