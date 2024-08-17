package com.aoldacloud.console.security.service;

import com.aoldacloud.console.security.entity.CloudSession;
import com.aoldacloud.console.security.entity.KeystoneUserDetails;
import lombok.RequiredArgsConstructor;
import org.openstack4j.model.identity.v3.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeystoneUserDetailsService implements UserDetailsService {

  private final RedisTemplate<String, CloudSession> cloudSessionRedisTemplate;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String authToken = username;
    CloudSession session = cloudSessionRedisTemplate.opsForValue().get(authToken);

    if (session == null) {
      throw new UsernameNotFoundException("Keystone 사용자 정보를 찾을 수 없습니다.");
    }

    return new KeystoneUserDetails(session, authToken);
  }
}