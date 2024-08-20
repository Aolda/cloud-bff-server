package com.aoldacloud.console.global.repository;

import com.aoldacloud.console.domain.heimdall.Proxy;
import com.aoldacloud.console.domain.heimdall.dto.ProxyCreateDto;
import com.aoldacloud.console.domain.heimdall.dto.ProxyUpdateDto;
import com.aoldacloud.console.global.feign.*;
import com.aoldacloud.console.mapper.ProxyMapper;
import com.aoldacloud.console.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HeimdallRepository {

  private final HeimdallClient heimdallClient;

  public Proxy createProxy(ProxyCreateDto proxyCreateDto) {
    try {
      String userName = SecurityUtils.getAuthenticatedUserDetails().getUsername();
      HeimdallCreateProxyDto heimdallCreateProxyDto = new HeimdallCreateProxyDto(userName, proxyCreateDto);
      HeimdallProxyDto heimdall = heimdallClient.addProxy(heimdallCreateProxyDto);
      return ProxyMapper.dtoToEntity(heimdall);
    } catch (Exception ex) {
      log.error("프록시 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("프록시 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public Proxy updateProxy(ProxyUpdateDto proxyUpdateDto) {
    try {
      String userName = SecurityUtils.getAuthenticatedUserDetails().getUsername();
      HeimdallUpdateProxyDto heimdallUpdateProxyDto = new HeimdallUpdateProxyDto(userName, proxyUpdateDto);
      HeimdallProxyDto heimdall = heimdallClient.updateProxy(heimdallUpdateProxyDto);
      return ProxyMapper.dtoToEntity(heimdall);
    } catch (Exception ex) {
      log.error("프록시 업데이트 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("프록시 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteProxy(String proxyId) {
    try {
      String userName = SecurityUtils.getAuthenticatedUserDetails().getUsername();
      HeimdallDeleteProxyDto heimdallDeleteProxyDto = new HeimdallDeleteProxyDto(userName, proxyId);
      heimdallClient.deleteProxy(heimdallDeleteProxyDto);
    } catch (Exception ex) {
      log.error("프록시 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("프록시 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<Proxy> listProxies() {
    try {
      String userName = SecurityUtils.getAuthenticatedUserDetails().getUsername();
      List<HeimdallProxyDto> heimdallProxyDtos = heimdallClient.readProxyList(userName);
      return ProxyMapper.dtosToEntities(heimdallProxyDtos);
    } catch (Exception ex) {
      log.error("프록시 목록 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("프록시 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Proxy getProxyById(String proxyId) {
    try {
      HeimdallProxyDto heimdall = heimdallClient.readProxy(Long.valueOf(proxyId));
      return ProxyMapper.dtoToEntity(heimdall);
    } catch (Exception ex) {
      log.error("프록시 상세 정보 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("프록시 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }
}
