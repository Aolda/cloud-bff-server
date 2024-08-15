package com.aoldacloud.console.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginDto {

  @Schema(description = "로그인에 사용되는 사용자 이름.", example = "user@example.com")
  private final String username;

  @Schema(description = "로그인에 사용되는 비밀번호.", example = "password123")
  private final String password;
}