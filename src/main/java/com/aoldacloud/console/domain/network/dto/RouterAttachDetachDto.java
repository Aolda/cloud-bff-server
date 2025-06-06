package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterAttachDetachDto {
  private String routerId;
  private String subnetId;
}
