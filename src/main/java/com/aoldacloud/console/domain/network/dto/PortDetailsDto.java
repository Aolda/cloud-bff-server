package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortDetailsDto {

  private static final Logger logger = LoggerFactory.getLogger(PortDetailsDto.class);

  private String id;
  private State state;
  private String networkId;
  private Set<? extends IP> fixedIps;
  private Set<? extends AllowedAddressPair> allowedAddressPairs;
  private String macAddress;
  private List<String> securityGroups;
  private Map<String, Object> profile;

  public PortDetailsDto(Port port) {
    this.id = port.getId();
    this.state = port.getState();
    this.networkId = port.getNetworkId();
    this.fixedIps = port.getFixedIps();
    this.allowedAddressPairs = port.getAllowedAddressPairs();
    this.macAddress = port.getMacAddress();
    this.securityGroups = port.getSecurityGroups();
    this.profile = port.getProfile();
  }
}
