package com.cloudboot.room_reservation.util.jwt.filter;

import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.MemberDTO;
import com.cloudboot.room_reservation.member.enumerate.Role;
import com.cloudboot.room_reservation.member.service.ReissueService;
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
    private final ReissueService reissueService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. access token 추출
        String accessToken = null;

        // 1-1. Authorization: Bearer 형식
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            accessToken = authorization.substring(7);
            log.info("accessToken from Authorization header = {}", accessToken);
        }

        // 1-2. access 헤더 직접
        if (accessToken == null) {
            accessToken = request.getHeader("access");
            if (accessToken != null) {
                log.info("accessToken from custom header = {}", accessToken);
            }
        }

        // 1-3. 쿠키
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

        // 2. access token 없으면 다음 필터로
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 3. access token 만료되었는지 확인
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // access 만료 → refresh로 재발급 시도
            boolean reissued = reissueService.reissue(request, response);

            if (!reissued) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired. Please login again.");
                return;
            }

            // 새 accessToken 재설정
            accessToken = response.getHeader("access");

            if (accessToken == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // 4. 토큰에서 사용자 정보 추출
        Long id = jwtUtil.getId(accessToken);
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        MemberDTO memberDTO = MemberDTO.of(id, username, "temp", Role.valueOf(role));
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(memberDTO);

        // 5. Spring Security 인증 객체 설정
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 6. 다음 필터 진행
        filterChain.doFilter(request, response);
    }
}
