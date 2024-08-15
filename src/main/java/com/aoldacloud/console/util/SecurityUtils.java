package com.aoldacloud.console.util;

import com.aoldacloud.console.security.entity.KeystoneUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
}
