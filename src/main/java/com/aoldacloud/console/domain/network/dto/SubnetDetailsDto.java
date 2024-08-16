package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Pool;
import org.openstack4j.model.network.Subnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubnetDetailsDto {

  private static final Logger logger = LoggerFactory.getLogger(SubnetDetailsDto.class);

  private String id;
  private String networkId;
  private boolean isDHCPEnabled;
  private List<? extends Pool> allocationPools;
  private List<? extends HostRoute> hostRoutes;
  private IPVersionType ipVersion;
  private String gateway;
  private String cidr;

  public SubnetDetailsDto(Subnet subnet) {
    this.id = subnet.getId();
    this.networkId = subnet.getNetworkId();
    this.isDHCPEnabled = subnet.isDHCPEnabled();
    this.allocationPools = subnet.getAllocationPools();
    this.hostRoutes = subnet.getHostRoutes();
    this.ipVersion = subnet.getIpVersion();
    this.gateway = subnet.getGateway();
    this.cidr = subnet.getCidr();
  }
}
