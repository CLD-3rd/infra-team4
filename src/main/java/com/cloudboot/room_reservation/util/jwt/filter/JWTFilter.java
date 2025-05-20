package com.cloudboot.room_reservation.util.jwt.filter;

import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.MemberDTO;
import com.cloudboot.room_reservation.member.enumerate.Role;
import com.cloudboot.room_reservation.util.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = null;

        // 1. Authorization: Bearer 형식 우선 처리 -- spring security 형식
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            accessToken = authorization.substring(7);
            log.info("accessToken from Authorization header = {}", accessToken);
        }

        // 2. access 헤더에 담겨 있는 경우 처리
        if (accessToken == null) {
            accessToken = request.getHeader("access");
            if (accessToken != null) {
                log.info("accessToken from custom header = {}", accessToken);
            }
        }

        // 3. 쿠키에서 access 토큰 확인
        if (accessToken == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("access".equals(cookie.getName())) {
                        accessToken = cookie.getValue();
                        log.info("accessToken from cookie = {}", accessToken);
                    }
                }
            }
        }


        // access Token 없으면 다음 필터 이동
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return ;
        }
        // access Token은 있는데 만료 되었으면 다음 필터 이동 없이, 바로 종료
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        }

        // JWTUtil을 통해서 access Token값 읽기
        Long id = jwtUtil.getId(accessToken);
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        MemberDTO memberDTO = MemberDTO.of(id, username, "temp", Role.valueOf(role));
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(memberDTO);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customMemberDetails,
                null, customMemberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
