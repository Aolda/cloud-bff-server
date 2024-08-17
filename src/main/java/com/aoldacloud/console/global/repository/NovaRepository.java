package com.aoldacloud.console.global.repository;

import com.aoldacloud.console.domain.compute.dto.ServerCreateDto;
import com.aoldacloud.console.domain.compute.dto.ServerUpdateDto;
import com.aoldacloud.console.global.OpenstackService;
import com.aoldacloud.console.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.compute.AbsoluteLimit;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.compute.domain.NovaServerCreate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NovaRepository {

  public Server createServer(ServerCreateDto serverCreateDto) {
    try {
      OSClientV3 os = getClient();
      var builder = new NovaServerCreate().toBuilder()
              .name(serverCreateDto.getName())
              .flavor(serverCreateDto.getFlavorId())
              .image(serverCreateDto.getImageId());

      if (serverCreateDto.getMetadata() != null) {
        builder.addMetadata(serverCreateDto.getMetadata()).build();
      }

      if (serverCreateDto.getPersonalities() != null) {
        serverCreateDto.getPersonalities().forEach(builder::addPersonality);
      }

      ServerCreate sc = builder.build();
      return os.compute().servers().boot(sc);
    } catch (Exception ex) {
      log.error("가상머신 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("가상머신 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public Server updateServer(ServerUpdateDto serverUpdateDto) {
    try {
      OSClientV3 os = getClient();
      Server server = os.compute().servers().get(serverUpdateDto.getServerId());
      if (serverUpdateDto.getMetadata() != null) {
        os.compute().servers().updateMetadata(server.getId(), serverUpdateDto.getMetadata());
      }
      return os.compute().servers().get(serverUpdateDto.getServerId());
    } catch (Exception ex) {
      log.error("가상머신 업데이트 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("가상머신 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteServer(String serverId) {
    try {
      OSClientV3 os = getClient();
      os.compute().servers().delete(serverId);
    } catch (Exception ex) {
      log.error("가상머신 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("가상머신 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Server> listServers() {
    try {
      OSClientV3 os = getClient();
      return os.compute().servers().list();
    } catch (Exception ex) {
      log.error("가상머신 목록 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("가상머신 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Server getServerById(String serverId) {
    try {
      OSClientV3 os = getClient();
      return os.compute().servers().get(serverId);
    } catch (Exception ex) {
      log.error("가상머신 상세 정보 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("가상머신 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public void performServerAction(String serverId, String action) {
    try {
      OSClientV3 os = getClient();
      Action serverAction = Action.valueOf(action.toUpperCase());
      os.compute().servers().action(serverId, serverAction);
    } catch (Exception ex) {
      log.error("서버 액션 실행 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서버 액션 실행 중 오류가 발생했습니다.", ex);
    }
  }

  public String createSnapshot(String serverId, String snapshotName) {
    try {
      OSClientV3 os = getClient();
      return os.compute().servers().createSnapshot(serverId, snapshotName);
    } catch (Exception ex) {
      log.error("스냅샷 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("스냅샷 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public Map<String, String> getServerMetadata(String serverId) {
    try {
      OSClientV3 os = getClient();
      return os.compute().servers().getMetadata(serverId);
    } catch (Exception ex) {
      log.error("서버 메타데이터 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서버 메타데이터 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public void updateServerMetadata(String serverId, Map<String, String> metadata) {
    try {
      OSClientV3 os = getClient();
      os.compute().servers().updateMetadata(serverId, metadata);
    } catch (Exception ex) {
      log.error("서버 메타데이터 업데이트 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서버 메타데이터 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteServerMetadataItem(String serverId, String key) {
    try {
      OSClientV3 os = getClient();
      os.compute().servers().deleteMetadataItem(serverId, key);
    } catch (Exception ex) {
      log.error("서버 메타데이터 항목 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서버 메타데이터 항목 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  private OSClientV3 getClient() {
    return OpenstackService.getClient();
  }
}
