package com.aoldacloud.console.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.identity.v3.User;

@Builder
@Getter
@Schema(description = "사용자 정보를 포함한 DTO.")
public class UserDetailDto {
  @Schema(description = "Keystone 사용자 id")
  private final String id;
  @Schema(description = "Keystone 사용자 이름")
  private final String name;
  @Schema(description = "Keystone 사용자 이메일")
  private final String email;
  @Schema(description = "사용자 프로젝트 정보")
  private final UserDetailProjectDto project;

  public static UserDetailDto fromUser(User user) {
    return UserDetailDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .project(user.getDefaultProjectId() == null ? null : new UserDetailProjectDto(user.getDefaultProjectId(), null))
            .build();
  }

  public static UserDetailDto fromToken(Token token) {
    User user = token.getUser();
    return UserDetailDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .project(token.getProject() == null ? null : new UserDetailProjectDto(token.getProject().getId(), token.getProject().getName()))
            .build();
  }

  @Getter
  @RequiredArgsConstructor
  private static class UserDetailProjectDto {
    private final String id;
    private final String name;
  }
}
