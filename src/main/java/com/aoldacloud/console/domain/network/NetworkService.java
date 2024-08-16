package com.aoldacloud.console.domain.network;

import com.aoldacloud.console.domain.compute.dto.ServerDetailsDto;
import com.aoldacloud.console.domain.network.dto.SubnetCreateDto;
import com.aoldacloud.console.domain.network.dto.SubnetDetailsDto;
import com.aoldacloud.console.global.repository.NeutronRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public void deleteSubnet(String serverId) {
    try {
      log.info("서브넷 삭제 요청: {}", serverId);
      neutronRepository.deleteSubnet(serverId);
      log.info("서브넷 삭제 성공: {}", serverId);
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

  public SubnetDetailsDto getSubnetDetails(String serverId) {
    try {
      log.info("서브넷 상세 정보 요청: {}", serverId);
      Subnet subnet = neutronRepository.getSubnetById(serverId);
      if (subnet == null) {
        return null;
      }
      return new SubnetDetailsDto(subnet);
    } catch (RuntimeException ex) {
      log.error("서브넷 상세 정보 조회 실패: {}", ex.getMessage());
      throw new RuntimeException("서브넷 상세 정보 조회 중 오류가 발생했습니다.", ex);
    }
  }
}
