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
public class ServerUpdateDto {
  private String serverId;
  private Map<String, String> metadata;
  private Map<String, String> personalities;
}