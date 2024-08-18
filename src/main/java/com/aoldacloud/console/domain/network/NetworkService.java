package com.aoldacloud.console.domain.network;

import com.aoldacloud.console.domain.network.dto.*;
import com.aoldacloud.console.global.repository.NeutronRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NetworkService {

  private final NeutronRepository neutronRepository;

  public SubnetDetailsDto createSubnet(SubnetCreateDto subnetCreateDto) {
    try {
      log.info("서브넷 생성 요청: {}", subnetCreateDto.getName());
      Subnet createdSubnet = neutronRepository.createSubnet(subnetCreateDto);
      log.info("서브넷 생성 성공: {}", createdSubnet.getId());
      return new SubnetDetailsDto(createdSubnet);
    } catch (RuntimeException ex) {
      log.error("서브넷 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("서브넷 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteSubnet(String subnetId) {
    try {
      log.info("서브넷 삭제 요청: {}", subnetId);
      neutronRepository.deleteSubnet(subnetId);
      log.info("서브넷 삭제 성공: {}", subnetId);
    } catch (RuntimeException ex) {
      log.error("서브넷 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("서브넷 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<SubnetDetailsDto> listSubnets() {
    try {
      log.info("서브넷 목록 요청");
      return neutronRepository.listSubnets().stream()
              .map(SubnetDetailsDto::new)
              .collect(Collectors.toList());
    } catch (RuntimeException ex) {
      log.error("서브넷 목록 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("서브넷 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public SubnetDetailsDto getSubnetDetails(String subnetId) {
    try {
      log.info("서브넷 상세 정보 요청: {}", subnetId);
      Subnet subnet = neutronRepository.getSubnetById(subnetId);
      if (subnet == null) {
        return null;
      }
      return new SubnetDetailsDto(subnet);
    } catch (RuntimeException ex) {
      log.error("서브넷 상세 정보 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("서브넷 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public PortDetailsDto createPort(PortCreateDto portCreateDto) {
    try {
      log.info("포트 생성 요청: {}", portCreateDto.getName());
      Port createdPort = neutronRepository.createPort(portCreateDto);
      log.info("포트 생성 성공: {}", createdPort.getId());
      return new PortDetailsDto(createdPort);
    } catch (RuntimeException ex) {
      log.error("포트 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("포트 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public PortDetailsDto updatePort(PortUpdateDto portUpdateDto) {
    try {
      log.info("포트 업데이트 요청: {}", portUpdateDto.getPortId());
      Port updatedPort = neutronRepository.updatePort(portUpdateDto);
      log.info("포트 업데이트 성공: {}", updatedPort.getId());
      return new PortDetailsDto(updatedPort);
    } catch (RuntimeException ex) {
      log.error("포트 업데이트 실패: {}", ex.getMessage());
      throw new RuntimeException("포트 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deletePort(String portId) {
    try {
      log.info("포트 삭제 요청: {}", portId);
      neutronRepository.deletePort(portId);
      log.info("포트 삭제 성공: {}", portId);
    } catch (RuntimeException ex) {
      log.error("포트 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("포트 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<PortDetailsDto> listPorts() {
    try {
      log.info("포트 목록 요청");
      return neutronRepository.listPorts().stream()
              .map(PortDetailsDto::new)
              .collect(Collectors.toList());
    } catch (RuntimeException ex) {
      log.error("포트 목록 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("포트 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public PortDetailsDto getPortDetails(String portId) {
    try {
      log.info("포트 상세 정보 요청: {}", portId);
      Port port = neutronRepository.getPortById(portId);
      if (port == null) {
        return null;
      }
      return new PortDetailsDto(port);
    } catch (RuntimeException ex) {
      log.error("포트 상세 정보 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("포트 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public RouterDetailsDto createRouter(RouterCreateDto routerCreateDto) {
    try {
      log.info("라우터 생성 요청: {}", routerCreateDto.getName());
      Router createdRouter = neutronRepository.createRouter(routerCreateDto);
      log.info("라우터 생성 성공: {}", createdRouter.getId());
      return new RouterDetailsDto(createdRouter);
    } catch (RuntimeException ex) {
      log.error("라우터 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("포트 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public RouterDetailsDto updateRouter(RouterUpdateDto routerUpdateDto) {
    try {
      log.info("라우터 업데이트 요청: {}", routerUpdateDto.getRouterId());
      Router updatedRouter = neutronRepository.updateRouter(routerUpdateDto);
      log.info("라우터 업데이트 성공: {}", updatedRouter.getId());
      return new RouterDetailsDto(updatedRouter);
    } catch (RuntimeException ex) {
      log.error("라우터 업데이트 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteRouter(String routerId) {
    try {
      log.info("라우터 삭제 요청: {}", routerId);
      neutronRepository.deleteRouter(routerId);
      log.info("라우터 삭제 성공: {}", routerId);
    } catch (RuntimeException ex) {
      log.error("라우터 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 삭제 중 오류가 발생했습니다.", ex);
    }
  }

  public List<RouterDetailsDto> listRouters() {
    try {
      log.info("라우터 목록 요청");
      return neutronRepository.listRouters().stream()
              .map(RouterDetailsDto::new)
              .collect(Collectors.toList());
    } catch (RuntimeException ex) {
      log.error("라우터 목록 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public RouterDetailsDto getRouterDetails(String routerId) {
    try {
      log.info("라우터 상세 정보 요청: {}", routerId);
      Router router = neutronRepository.getRouterById(routerId);
      if (router == null) {
        return null;
      }
      return new RouterDetailsDto(router);
    } catch (RuntimeException ex) {
      log.error("라우터 상세 정보 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }

  public void attachRouter(RouterAttachDetachDto routerAttachDetachDto) {
    try {
      log.info("라우터 인터페이스 연결 요청: {}", routerAttachDetachDto.getRouterId());
      neutronRepository.attachRouter(routerAttachDetachDto);
    } catch (RuntimeException ex) {
      log.error("라우터 인터페이스 연결 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 인터페이스 연결 중 오류가 발생했습니다.", ex);
    }
  }

  public void detachRouter(RouterAttachDetachDto routerAttachDetachDto) {
    try {
      log.info("라우터 인터페이스 연결 해제 요청: {}", routerAttachDetachDto.getRouterId());
      neutronRepository.detachRouter(routerAttachDetachDto);
    } catch (RuntimeException ex) {
      log.error("라우터 인터페이스 연결 해제 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 인터페이스 연결 해제 중 오류가 발생했습니다.", ex);
    }
  }

  public void toggleStateRouter(RouterToggleStateDto routerToggleStateDto) {
    try {
      log.info("라우터 상태 변경 요청: {}", routerToggleStateDto.getRouterId());
      neutronRepository.toggleStateRouter(routerToggleStateDto);
    } catch (RuntimeException ex) {
      log.error("라우터 상태 변경 실패: {}", ex.getMessage());
      throw new RuntimeException("라우터 상태 변경 중 오류가 발생했습니다.", ex);
    }
  }

  public List<FloatingIpDetailsDto> listFloatingIps() {
    try {
      log.info("Floating IP 목록 요청");
      return neutronRepository.listFloatingIps().stream()
              .map(FloatingIpDetailsDto::new)
              .collect(Collectors.toList());
    } catch (RuntimeException ex) {
      log.error("Floating IP 목록 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("Floating IP 목록 조회 중 오류가 발생했습니다.", ex);
    }
  }


  public FloatingIpDetailsDto createFloatingIp(FloatingIpCreateDto floatingIpCreateDto) {
    try {
      log.info("Floating IP 생성 요청: {}", floatingIpCreateDto.getNetworkId());
      NetFloatingIP createdFIp = neutronRepository.createFloatingIp(floatingIpCreateDto);
      log.info("Floating IP 생성 성공: {}", createdFIp.getId());
      return new FloatingIpDetailsDto(createdFIp);
    } catch (RuntimeException ex) {
      log.error("Floating IP 생성 실패: {}", ex.getMessage());
      throw new RuntimeException("Floating IP 생성 중 오류가 발생했습니다.", ex);
    }
  }

  public void deleteFloatingIp(String floatingIpId) {
    try {
      log.info("Floating IP 삭제 요청: {}", floatingIpId);
      neutronRepository.deleteFloatingIp(floatingIpId);
      log.info("Floating IP 삭제 성공: {}", floatingIpId);
    } catch (RuntimeException ex) {
      log.error("Floating IP 삭제 실패: {}", ex.getMessage());
      throw new RuntimeException("Floating IP 삭제 중 오류가 발생했습니다.", ex);
    }
  }
}
