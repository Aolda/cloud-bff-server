package com.aoldacloud.console.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openstack4j.model.identity.v3.Project;

import java.util.List;
import java.util.Objects;

@Getter
@Builder
@Schema(description = "현재 프로젝트와 사용 가능한 프로젝트 목록을 포함하는 DTO.")
public class ProjectInfoDto {

  @Schema(description = "현재 인증된 사용자의 현재 프로젝트.")
  private final ProjectInfoDetailDto current;

  @Schema(description = "현재 인증된 사용자가 접근할 수 있는 프로젝트 목록.")
  private final List<ProjectInfoDetailDto> availables;

  public static ProjectInfoDto fromProjects(Project currentProject, List<? extends Project> availableProjects) {
    return ProjectInfoDto.builder()
            .availables(
                    availableProjects.stream()
                            .filter(project -> !Objects.equals(project.getId(), currentProject.getId()))
                            .map(ProjectInfoDetailDto::fromProject)
                            .toList())
            .current(ProjectInfoDetailDto.fromProject(currentProject))
            .build();
  }

  @Getter
  @RequiredArgsConstructor
  private static class ProjectInfoDetailDto {
    private final String id;
    private final String name;
    public static ProjectInfoDetailDto fromProject(Project project) {
      return new ProjectInfoDetailDto(project.getId(), project.getName());
    }
  }
}