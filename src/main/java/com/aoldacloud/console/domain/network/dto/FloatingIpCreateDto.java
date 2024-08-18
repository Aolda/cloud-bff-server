package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloatingIpCreateDto {

  private static final Logger logger = LoggerFactory.getLogger(FloatingIpCreateDto.class);

  private String portId;
  private String networkId;
}
