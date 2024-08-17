package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.IP;
import org.openstack4j.model.network.IPVersionType;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortCreateDto {
  private String name;
  private String networkId;
  private Set<? extends IP> fixedIps;
}
