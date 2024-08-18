package com.aoldacloud.console.global.repository;

import com.aoldacloud.console.domain.network.dto.*;
import com.aoldacloud.console.global.OpenstackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.network.*;
import org.openstack4j.model.network.options.PortListOptions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NeutronRepository {

  public Subnet createSubnet(SubnetCreateDto subnetCreateDto) {
    try {
      OSClientV3 os = getClient();
        return os.networking().subnet().create(Builders.subnet()
                .name(subnetCreateDto.getName())
                .networkId(subnetCreateDto.getNetworkId())
                .tenantId("default") // fixed
                .addPool(subnetCreateDto.getAddPool().getFirst(), subnetCreateDto.getAddPool().getLast())
                .ipVersion(subnetCreateDto.getIpVersion())
                .cidr(subnetCreateDto.getCidr())
                .build());
    } catch (Exception ex) {
      log.error("서브넷 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서브넷 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteSubnet(String subnetId) {
    try {
      OSClientV3 os = getClient();
      os.networking().subnet().delete(subnetId);
    } catch (Exception ex) {
      log.error("서브넷 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서브넷 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Subnet> listSubnets() {
    try {
      OSClientV3 os = getClient();
      return os.networking().subnet().list();
    } catch (Exception ex) {
      log.error("서브넷 목록 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서브넷 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Subnet getSubnetById(String subnetId) {
    try {
      OSClientV3 os = getClient();
      return os.networking().subnet().get(subnetId);
    } catch (Exception ex) {
      log.error("서브넷 상세 정보 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("서브넷 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Port createPort(PortCreateDto portCreateDto) {
    try {
      OSClientV3 os = getClient();
      String fixedIpAddress = portCreateDto.getFixedIps().stream().map(IP::getIpAddress).toString();
      String fixedSubnetId = portCreateDto.getFixedIps().stream().map(IP::getSubnetId).toString();

      return os.networking().port().create(Builders.port()
              .name(portCreateDto.getName())
              .networkId(portCreateDto.getNetworkId())
              .fixedIp(fixedIpAddress, fixedSubnetId)
              .build());
    } catch (Exception ex) {
      log.error("포트 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("포트 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public Port updatePort(PortUpdateDto portUpdateDto) {
    try {
      OSClientV3 os = getClient();
      String fixedIpAddress = portUpdateDto.getFixedIps().stream().map(IP::getIpAddress).toString();
      String fixedSubnetId = portUpdateDto.getFixedIps().stream().map(IP::getSubnetId).toString();
      Port port = os.networking().port().get(portUpdateDto.getPortId());
      port = os.networking().port().update(
              Builders.port()
                      .name(portUpdateDto.getName())
                      .networkId(portUpdateDto.getNetworkId())
                      .fixedIp(fixedIpAddress, fixedSubnetId)
                      .build()
      );
      return port;
    } catch (Exception ex) {
      log.error("포트 업데이트 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("포트 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deletePort(String subnetId) {
    try {
      OSClientV3 os = getClient();
      os.networking().subnet().delete(subnetId);
    } catch (Exception ex) {
      log.error("포트 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("포트 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Port> listPorts() {
    try {
      OSClientV3 os = getClient();
      return os.networking().port().list();
    } catch (Exception ex) {
      log.error("포트 목록 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("포트 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Port getPortById(String subnetId) {
    try {
      OSClientV3 os = getClient();
      return os.networking().port().get(subnetId);
    } catch (Exception ex) {
      log.error("포트 상세 정보 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("포트 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Router createRouter(RouterCreateDto routerCreateDto) {
    try {
      OSClientV3 os = getClient();

      return os.networking().router().create(Builders.router()
              .name(routerCreateDto.getName())
              .adminStateUp(routerCreateDto.isAdminStateUp())
              .externalGateway(routerCreateDto.getNetworkId())
              .route(routerCreateDto.getDestination(), routerCreateDto.getNextHop())
              .build());
    } catch (Exception ex) {
      log.error("라우터 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public Router updateRouter(RouterUpdateDto routerUpdateDto) {
    try {
      OSClientV3 os = getClient();
      Router router = os.networking().router().get(routerUpdateDto.getRouterId());
      router = os.networking().router().update(
              Builders.router()
                      .name(routerUpdateDto.getName())
                      .adminStateUp(routerUpdateDto.isAdminStateUp())
                      .externalGateway(routerUpdateDto.getNetworkId())
                      .route(routerUpdateDto.getDestination(), routerUpdateDto.getNextHop())
                      .build()
      );
      return router;
    } catch (Exception ex) {
      log.error("라우터 업데이트 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteRouter(String routerId) {
    try {
      OSClientV3 os = getClient();
      os.networking().router().delete(routerId);
    } catch (Exception ex) {
      log.error("라우터 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Router> listRouters() {
    try {
      OSClientV3 os = getClient();
      return os.networking().router().list();
    } catch (Exception ex) {
      log.error("라우터 목록 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public Router getRouterById(String routerId) {
    try {
      OSClientV3 os = getClient();
      return os.networking().router().get(routerId);
    } catch (Exception ex) {
      log.error("라우터 상세 정보 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public void attachRouter(RouterAttachDetachDto routerAttachDetachDto) {
    try {
      OSClientV3 os = getClient();
      os.networking().router().attachInterface(
              routerAttachDetachDto.getRouterId(),
              AttachInterfaceType.SUBNET,
              routerAttachDetachDto.getSubnetId()
      );
    } catch (Exception ex) {
      log.error("라우터 인터페이스 연결 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 인터페이스 연결 중 오류가 발생했습니다.", ex);
    }
  }

  public void detachRouter(RouterAttachDetachDto routerAttachDetachDto) {
    try {
      OSClientV3 os = getClient();
      os.networking().router().detachInterface(
              routerAttachDetachDto.getRouterId(),
              routerAttachDetachDto.getSubnetId(),
              null
      );
    } catch (Exception ex) {
      log.error("라우터 인터페이스 연결 해제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 인터페이스 연결 해제 중 오류가 발생했습니다.", ex);
    }
  }

  public void toggleStateRouter(RouterToggleStateDto routerToggleStateDto) {
    try {
      OSClientV3 os = getClient();
      os.networking().router().toggleAdminStateUp(
              routerToggleStateDto.getRouterId(),
              routerToggleStateDto.isAdminStateUp()
      );
    } catch (Exception ex) {
      log.error("라우터 상태 변경 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("라우터 상태 변경 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends NetFloatingIP> listFloatingIps() {
    try {
      OSClientV3 os = getClient();
      return os.networking().floatingip().list();
    } catch (Exception ex) {
      log.error("Floating IP 목록 조회 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("Floating IP 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public NetFloatingIP createFloatingIp(FloatingIpCreateDto floatingIpCreateDto) {
    try {
      OSClientV3 os = getClient();

      NetFloatingIP fip = Builders.netFloatingIP()
              .portId(floatingIpCreateDto.getPortId())
              .floatingNetworkId(floatingIpCreateDto.getNetworkId())
              .build();
      fip = os.networking().floatingip().create(fip);
      return fip;
    } catch (Exception ex) {
      log.error("Floating IP 생성 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("Floating IP 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteFloatingIp(String floatingIpId) {
    try {
      OSClientV3 os = getClient();
      os.networking().floatingip().delete(floatingIpId);
    } catch (Exception ex) {
      log.error("Floating IP 삭제 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("Floating IP 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  private OSClientV3 getClient() {
    return OpenstackService.getClient();
  }
}
