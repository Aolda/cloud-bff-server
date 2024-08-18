package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.IP;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterCreateDto {
  private String name;
  private boolean adminStateUp;
  private String networkId;
  private String destination;
  private String nextHop;
}
