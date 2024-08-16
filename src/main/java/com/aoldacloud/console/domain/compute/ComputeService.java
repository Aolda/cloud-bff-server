package com.aoldacloud.console.domain.compute;

import com.aoldacloud.console.domain.compute.dto.ServerCreateDto;
import com.aoldacloud.console.domain.compute.dto.ServerDetailsDto;
import com.aoldacloud.console.domain.compute.dto.ServerUpdateDto;
import com.aoldacloud.console.global.repository.NovaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.model.compute.Server;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComputeService {

  private final NovaRepository novaRepository;

  public ServerDetailsDto createServer(ServerCreateDto serverCreateDto) {
    try {
      log.info("가상머신 생성 요청: {}", serverCreateDto.getName());
      Server createdServer = novaRepository.createServer(serverCreateDto);
      log.info("가상머신 생성 성공: {}", createdServer.getId());
      return ServerDetailsDto.fromServer(createdServer);
    } catch (RuntimeException ex) {
      log.error("가상머신 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("가상머신 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public ServerDetailsDto updateServer(ServerUpdateDto serverUpdateDto) {
    try {
      log.info("가상머신 업데이트 요청: {}", serverUpdateDto.getServerId());
      Server updatedServer = novaRepository.updateServer(serverUpdateDto);
      log.info("가상머신 업데이트 성공: {}", updatedServer.getId());
      return ServerDetailsDto.fromServer(updatedServer);
    } catch (RuntimeException ex) {
      log.error("가상머신 업데이트 실패: {}", ex.getMessage());
      throw new RuntimeException("가상머신 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteServer(String serverId) {
    try {
      log.info("가상머신 삭제 요청: {}", serverId);
      novaRepository.deleteServer(serverId);
      log.info("가상머신 삭제 성공: {}", serverId);
    } catch (RuntimeException ex) {
      log.error("가상머신 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("가상머신 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<ServerDetailsDto> listServers() {
    try {
      log.info("가상머신 목록 요청");
      return novaRepository.listServers().stream()
              .map(ServerDetailsDto::fromServer)
              .collect(Collectors.toList());
    } catch (RuntimeException ex) {
      log.error("가상머신 목록 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("가상머신 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public ServerDetailsDto getServerDetails(String serverId) {
    try {
      log.info("가상머신 상세 정보 요청: {}", serverId);
      Server server = novaRepository.getServerById(serverId);
      if (server == null) {
        return null;
      }
      return ServerDetailsDto.fromServer(server);
    } catch (RuntimeException ex) {
      log.error("가상머신 상세 정보 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("가상머신 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public void performServerAction(String serverId, String action) {
    try {
      log.info("서버 액션 요청: serverId={}, action={}", serverId, action);
      novaRepository.performServerAction(serverId, action);
      log.info("서버 액션 성공: serverId={}, action={}", serverId, action);
    } catch (RuntimeException ex) {
      log.error("서버 액션 실패: {}", ex.getMessage());
      throw new RuntimeException("서버 액션 중 오류가 발생했습니다.", ex);
    }
  }

  public String createSnapshot(String serverId, String snapshotName) {
    try {
      log.info("스냅샷 생성 요청: serverId={}, snapshotName={}", serverId, snapshotName);
      return novaRepository.createSnapshot(serverId, snapshotName);
    } catch (RuntimeException ex) {
      log.error("스냅샷 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("스냅샷 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public Map<String, String> getServerMetadata(String serverId) {
    try {
      log.info("서버 메타데이터 요청: serverId={}", serverId);
      return novaRepository.getServerMetadata(serverId);
    } catch (RuntimeException ex) {
      log.error("서버 메타데이터 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("서버 메타데이터 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public void updateServerMetadata(String serverId, Map<String, String> metadata) {
    try {
      log.info("서버 메타데이터 업데이트 요청: serverId={}", serverId);
      novaRepository.updateServerMetadata(serverId, metadata);
      log.info("서버 메타데이터 업데이트 성공: serverId={}", serverId);
    } catch (RuntimeException ex) {
      log.error("서버 메타데이터 업데이트 실패: {}", ex.getMessage());
      throw new RuntimeException("서버 메타데이터 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteServerMetadataItem(String serverId, String key) {
    try {
      log.info("서버 메타데이터 항목 삭제 요청: serverId={}, key={}", serverId, key);
      novaRepository.deleteServerMetadataItem(serverId, key);
      log.info("서버 메타데이터 항목 삭제 성공: serverId={}, key={}", serverId, key);
    } catch (RuntimeException ex) {
      log.error("서버 메타데이터 항목 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("서버 메타데이터 항목 삭제 중 오류가 발생했습니다.", ex);
    }
  }
}
