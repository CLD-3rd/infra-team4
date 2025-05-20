package com.cloudboot.room_reservation.member.service;

import com.cloudboot.room_reservation.member.entity.RefreshToken;
import com.cloudboot.room_reservation.member.repository.RefreshRepository;
import com.cloudboot.room_reservation.util.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public boolean reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) return false;

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return false;
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            return false;
        }

        if (!refreshRepository.existsByRefresh(refresh)) {
            return false;
        }

        Long id = jwtUtil.getId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", id, username, role,  600000L);
        String newRefresh = jwtUtil.createJwt("refresh", id, username, role,  86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return true;
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken entity = RefreshToken.of(username, refresh, date.toString());
        refreshRepository.save(entity);
    }
}
