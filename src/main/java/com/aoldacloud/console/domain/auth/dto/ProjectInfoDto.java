package com.aoldacloud.console.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.openstack4j.model.identity.v3.Project;

import java.util.List;

@Getter
@Builder
@Schema(description = "현재 프로젝트와 사용 가능한 프로젝트 목록을 포함하는 DTO.")
public class ProjectInfoDto {

  @Schema(description = "현재 인증된 사용자의 현재 프로젝트.")
  private final Project currentProject;

  @Schema(description = "현재 인증된 사용자가 접근할 수 있는 프로젝트 목록.")
  private final List<? extends Project> availableProjects;
}