package com.aoldacloud.console.security.filter;

import com.aoldacloud.console.global.OpenstackService;
import com.aoldacloud.console.global.ResponseWrapper;
import com.aoldacloud.console.security.entity.CloudSession;
import com.aoldacloud.console.security.entity.KeystoneUserDetails;
import com.aoldacloud.console.security.service.KeystoneUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.http.Cookie;

@Component
@RequiredArgsConstructor
public class KeystoneAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(KeystoneAuthenticationFilter.class);

  private final RedisTemplate<String, CloudSession> cloudSessionRedisTemplate;

  /**
   * 요청의 쿠키에서 인증 토큰을 가져옵니다.
   *
   * @param request HTTP 요청 객체
   * @return 인증 토큰 또는 null (쿠키에 인증 토큰이 없을 경우)
   */
  private String getAuthTokenFromCookies(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("X-AUTH-TOKEN".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  /**
   * 요청의 헤더에서 인증 토큰을 가져옵니다.
   *
   * @param request HTTP 요청 객체
   * @return 인증 토큰 또는 null (헤더에 인증 토큰이 없을 경우)
   */
  private String getAuthTokenFromHeader(HttpServletRequest request) {
    return request.getHeader("X-AUTH-TOKEN");
  }

  /**
   * Keystone 토큰을 검증하여 유효한지 확인합니다.
   *
   * @param authToken 인증 토큰
   * @return 유효한 경우 OSClientV3 객체, 그렇지 않으면 null
   */
  private CloudSession validateToken(String authToken) {
    try {
      return cloudSessionRedisTemplate.opsForValue().get(authToken);
    } catch (Exception ex) {
      logger.warn("Keystone 토큰 검증 실패: {}", ex.getMessage());
      return null;
    }
  }

  /**
   * 요청 경로가 인증이 필요 없는 경로인지 확인합니다.
   *
   * @param request HTTP 요청 객체
   * @return 인증이 필요 없는 경로이면 true, 그렇지 않으면 false
   */
  private boolean isPublicPath(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/api/v1.0/auth/login") || path.startsWith("/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/favicon.ico");
  }

  /**
   * 인증이 실패한 경우 401 Unauthorized 응답을 전송합니다.
   *
   * @param response HTTP 응답 객체
   * @param message 응답 메시지
   * @throws IOException 입출력 예외
   */
  private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
    ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(false, null, message);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(new ObjectMapper().writeValueAsString(responseWrapper));
  }

  /**
   * 요청에 대한 필터링 로직을 수행합니다.
   * 쿠키 또는 헤더에서 인증 토큰을 가져와 검증하며, 인증이 실패하면 401 응답을 전송합니다.
   *
   * @param request HTTP 요청 객체
   * @param response HTTP 응답 객체
   * @param filterChain 필터 체인
   * @throws ServletException 서블릿 예외
   * @throws IOException 입출력 예외
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    if (isPublicPath(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    String authToken = getAuthTokenFromHeader(request);

    if (authToken == null) {
      authToken = getAuthTokenFromCookies(request);
    }

    if (authToken != null) {
      CloudSession session = validateToken(authToken);
      if (session != null) {
        KeystoneUserDetails userDetails = new KeystoneUserDetails(session, authToken);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("{}, {}, {}, 사용자가 성공적으로 인증되었습니다.", request.getRemoteHost(), request.getRequestURI(), authToken);
      } else {
        logger.info("{}, {}, 유효하지 않은 Keystone 토큰입니다. {}", request.getRemoteHost(), request.getRequestURI(), authToken);
        sendUnauthorizedResponse(response, "유효하지 않은 인증 토큰입니다.");
        return;
      }
    } else {
      logger.info("{}, {}, {}, 인증 토큰이 존재하지 않습니다.", request.getRemoteHost(), request.getRequestURI(), authToken);
      sendUnauthorizedResponse(response, "인증 토큰이 존재하지 않습니다.");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
