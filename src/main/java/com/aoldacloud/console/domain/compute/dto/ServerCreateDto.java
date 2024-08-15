package com.aoldacloud.console.domain.compute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerCreateDto {
  private String name;
  private String flavorId;
  private String imageId;
  private Map<String, String> metadata;
  private Map<String, String> personalities;
}
