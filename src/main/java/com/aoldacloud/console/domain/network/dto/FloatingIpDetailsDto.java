package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloatingIpDetailsDto {

  private static final Logger logger = LoggerFactory.getLogger(FloatingIpDetailsDto.class);

  private String id;
  private String routerId;
  private String floatingNetworkId;
  private String floatingIpAddress;
  private String fixedIpAddress;
  private String portId;
  private String status;
  private Date createdAt;
  private Date updatedAt;

  public FloatingIpDetailsDto(NetFloatingIP netFloatingIP) {
    this.id = netFloatingIP.getId();
    this.routerId = netFloatingIP.getRouterId();
    this.floatingNetworkId = netFloatingIP.getFloatingNetworkId();
    this.floatingIpAddress = netFloatingIP.getFloatingIpAddress();
    this.fixedIpAddress = netFloatingIP.getFixedIpAddress();
    this.portId = netFloatingIP.getPortId();
    this.status = netFloatingIP.getStatus();
    this.createdAt = netFloatingIP.getCreatedAt();
    this.updatedAt = netFloatingIP.getUpdatedAt();
  }
}
