package com.aoldacloud.console.domain.heimdall;

import com.aoldacloud.console.domain.heimdall.dto.ProxyCreateDto;
import com.aoldacloud.console.domain.heimdall.dto.ProxyDetailsDto;
import com.aoldacloud.console.domain.heimdall.dto.ProxyUpdateDto;
import com.aoldacloud.console.domain.network.dto.*;
import com.aoldacloud.console.global.repository.HeimdallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HeimdallService {

  private final HeimdallRepository heimdallRepository;

  public ProxyDetailsDto createProxy(ProxyCreateDto proxyCreateDto) {
    try {
      log.info("프록시 생성 요청: {}", proxyCreateDto.getSubDomain());
      Proxy createdProxy = heimdallRepository.createProxy(proxyCreateDto);
      log.info("프록시 생성 성공: {}", createdProxy.getId());
      return new ProxyDetailsDto(createdProxy);
    } catch (RuntimeException ex) {
      log.error("프록시 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("프록시 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public ProxyDetailsDto updateProxy(ProxyUpdateDto proxyUpdateDto) {
    try {
      log.info("프록시 업데이트 요청: {}", proxyUpdateDto.getSubDomain());
      Proxy updatedProxy = heimdallRepository.updateProxy(proxyUpdateDto);
      log.info("프록시 업데이트 성공: {}", updatedProxy.getId());
      return new ProxyDetailsDto(updatedProxy);
    } catch (RuntimeException ex) {
      log.error("프록시 업데이트 실패: {}", ex.getMessage());
      throw new RuntimeException("프록시 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteProxy(String proxyId) {
    try {
      log.info("프록시 삭제 요청: {}", proxyId);
      heimdallRepository.deleteProxy(proxyId);
      log.info("프록시 삭제 성공: {}", proxyId);
    } catch (RuntimeException ex) {
      log.error("프록시 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("프록시 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<ProxyDetailsDto> listProxies() {
    try {
      log.info("프록시 목록 요청");
      return heimdallRepository.listProxies().stream()
              .map(ProxyDetailsDto::new)
              .collect(Collectors.toList());
    } catch (RuntimeException ex) {
      log.error("프록시 목록 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("프록시 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public ProxyDetailsDto getProxyDetails(String proxyId) {
    try {
      log.info("프록시 상세 정보 요청: {}", proxyId);
      Proxy proxy = heimdallRepository.getProxyById(proxyId);
      if (proxy == null) {
        return null;
      }
      return new ProxyDetailsDto(proxy);
    } catch (RuntimeException ex) {
      log.error("프록시 상세 정보 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("프록시 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }
}
