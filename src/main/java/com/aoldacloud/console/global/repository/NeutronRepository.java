package com.aoldacloud.console.global.repository;

import com.aoldacloud.console.domain.network.dto.SubnetCreateDto;
import com.aoldacloud.console.global.OpenstackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.network.Subnet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

  private OSClientV3 getClient() {
    return OpenstackService.getClient();
  }
}
