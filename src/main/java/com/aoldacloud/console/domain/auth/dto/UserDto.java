package com.aoldacloud.console.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.openstack4j.model.identity.v3.User;

@Getter
@Builder
@Schema(description = "사용자 인증 토큰을 포함한 DTO.")
public class UserDto {

  @Schema(description = "Keystone 사용자 정보")
  private final User user;

  @Schema(description = "사용자의 Keystone 인증 토큰", example = "ABC123XYZ")
  private final String authToken;
}