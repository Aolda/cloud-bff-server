package com.aoldacloud.console.util;

import com.aoldacloud.console.security.entity.CloudSession;
import com.aoldacloud.console.security.entity.KeystoneUserDetails;
import org.openstack4j.model.identity.v3.Token;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

public class SecurityUtils {

  /**
   * 인증된 사용자의 KeystoneUserDetails를 반환합니다.
   *
   * @return KeystoneUserDetails
   * @throws UsernameNotFoundException 인증된 사용자가 없을 경우 예외 발생
   */
  public static KeystoneUserDetails getAuthenticatedUserDetails() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof KeystoneUserDetails) {
      return (KeystoneUserDetails) principal;
    } else {
      throw new UsernameNotFoundException("인증된 사용자를 찾을 수 없습니다.");
    }
  }

  /**
   * 인증된 사용자의 CloudSession을 반환합니다.
   *
   * @return CloudSession
   * @throws UsernameNotFoundException 인증된 사용자가 없을 경우 예외 발생
   */
  public static CloudSession getCloudSession() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof KeystoneUserDetails) {
      return ((KeystoneUserDetails) principal).getCloudSession();
    } else {
      throw new UsernameNotFoundException("인증된 사용자를 찾을 수 없습니다.");
    }
  }


  /**
   * 인증된 사용자의 Token을 반환합니다.
   *
   * @return Token
   * @throws UsernameNotFoundException 인증된 사용자가 없을 경우 예외 발생
   */
  public static Token getSessionToken() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof KeystoneUserDetails) {
      return ((KeystoneUserDetails) principal).getCloudSession().getToken();
    } else {
      throw new UsernameNotFoundException("인증된 사용자를 찾을 수 없습니다.");
    }
  }


  /**
   * 문자열과 현재 시간을 기반으로 해시된 토큰을 생성합니다.
   *
   * @param str 문자열
   * @return 생성된 토큰
   */
  public static String generateToken(String str) {
    try {
      String data = str + LocalDateTime.now();
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
      String base64Token = Base64.getUrlEncoder().encodeToString(hash);

      StringBuilder tokenBuilder = new StringBuilder(base64Token);
      while (tokenBuilder.length() < 70) {
        tokenBuilder.append(base64Token);
      }

      return tokenBuilder.substring(0, Math.min(tokenBuilder.length(), 70));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("토큰 생성 중 오류가 발생했습니다.", e);
    }
  }
}
