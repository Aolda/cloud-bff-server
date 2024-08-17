package com.aoldacloud.console.global.repository;

import com.aoldacloud.console.domain.compute.dto.ServerUpdateDto;
import com.aoldacloud.console.domain.network.dto.PortCreateDto;
import com.aoldacloud.console.domain.network.dto.PortUpdateDto;
import com.aoldacloud.console.domain.network.dto.SubnetCreateDto;
import com.aoldacloud.console.global.OpenstackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.network.IP;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Subnet;
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

      os.networking().port().update(
              Builders.port()
                      .name(portUpdateDto.getName())
                      .networkId(portUpdateDto.getNetworkId())
                      .fixedIp(fixedIpAddress, fixedSubnetId)
                      .build()
      );
      return os.networking().port().get(portUpdateDto.getPortId());
    } catch (Exception ex) {
      log.error("가상머신 업데이트 중 오류 발생: {}", ex.getMessage());
      throw new RuntimeException("가상머신 업데이트 중 오류가 발생했습니다.", ex);
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

  private OSClientV3 getClient() {
    return OpenstackService.getClient();
  }
}
