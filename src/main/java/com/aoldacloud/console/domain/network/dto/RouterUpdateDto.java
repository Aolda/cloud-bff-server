package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterUpdateDto {
  private String routerId;
  private String name;
  private boolean adminStateUp;
  private String networkId;
  private String destination;
  private String nextHop;
}
