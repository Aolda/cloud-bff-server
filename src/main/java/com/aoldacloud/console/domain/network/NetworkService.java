package com.aoldacloud.console.domain.network;

import com.aoldacloud.console.domain.network.dto.*;
import com.aoldacloud.console.global.repository.NeutronRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.model.network.Port;
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
}
