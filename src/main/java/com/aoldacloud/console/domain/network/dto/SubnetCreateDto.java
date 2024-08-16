package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.IPVersionType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubnetCreateDto {
  private String name;
  private String networkId;
  private List<String> addPool;
  private IPVersionType ipVersion;
  private String cidr;
}
