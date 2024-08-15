package com.aoldacloud.console.security.entity;

import com.aoldacloud.console.domain.auth.dto.LoginDto;
import lombok.Getter;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.identity.v3.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class KeystoneUserDetails implements UserDetails {

  private final CloudSession cloudSession;

  public KeystoneUserDetails(CloudSession session) {
    this.cloudSession = session;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return cloudSession.getToken().getCredentials().getPassword(); // Password는 필요 없음 (토큰 기반)
  }

  @Override
  public String getUsername() {
    return cloudSession.getToken().getCredentials().getName(); // 사용자의 이름
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return cloudSession.getToken().getUser().isEnabled();
  }
}
