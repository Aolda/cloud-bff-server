package com.aoldacloud.console.domain.auth;

import com.aoldacloud.console.domain.auth.dto.LoginDto;
import com.aoldacloud.console.domain.auth.dto.ProjectInfoDto;
import com.aoldacloud.console.domain.auth.dto.UserDto;
import com.aoldacloud.console.global.repository.KeystoneRepository;
import com.aoldacloud.console.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final KeystoneRepository keystoneRepository;

  public UserDto loginWithCredentials(LoginDto loginDto) {
    try {
      logger.info("사용자 [{}] 로그인 시도 중", loginDto.getUsername());
      UserDto userDto = keystoneRepository.getUserByCredentials(loginDto.getUsername(), loginDto.getPassword());
      logger.info("사용자 [{}] 로그인 성공", loginDto.getUsername());
      return userDto;
    } catch (RuntimeException ex) {
      logger.error("사용자 [{}] 로그인 실패: {}", loginDto.getUsername(), ex.getMessage());
      throw ex;
    }
  }

  public Project getProjectDetails(String projectId) {
    try {
      logger.info("프로젝트 [{}] 상세 정보 요청 중", projectId);
      User user = getCurrentUser();
      Project project = keystoneRepository.getProjectById(user, projectId);
      logger.info("프로젝트 [{}] 상세 정보 가져오기 성공", projectId);
      return project;
    } catch (RuntimeException ex) {
      logger.error("프로젝트 [{}] 상세 정보 가져오기 실패: {}", projectId, ex.getMessage());
      throw ex;
    }
  }

  public UserDto updateUserInfo(User user) {
    try {
      logger.info("사용자 [{}] 정보 업데이트 시도 중", user.getId());
      UserDto dto = keystoneRepository.updateUser(user);
      logger.info("사용자 [{}] 정보 업데이트 성공", user.getId());
      return dto;
    } catch (RuntimeException ex) {
      logger.error("사용자 [{}] 정보 업데이트 실패: {}", user.getId(), ex.getMessage());
      throw ex;
    }
  }

  public List<? extends Project> getUserProjects() {
    try {
      logger.info("사용자 프로젝트 목록 요청 중");
      User user = getCurrentUser();
      List<? extends Project> projects = keystoneRepository.getProjects(user);
      logger.info("사용자 프로젝트 목록 가져오기 성공");
      return projects;
    } catch (RuntimeException ex) {
      logger.error("사용자 프로젝트 목록 가져오기 실패: {}", ex.getMessage());
      throw ex;
    }
  }

  public List<? extends Domain> getUserDomains() {
    try {
      logger.info("사용자 도메인 목록 요청 중");
      User user = getCurrentUser();
      List<? extends Domain> domains = keystoneRepository.getDomains(user);
      logger.info("사용자 도메인 목록 가져오기 성공");
      return domains;
    } catch (RuntimeException ex) {
      logger.error("사용자 도메인 목록 가져오기 실패: {}", ex.getMessage());
      throw ex;
    }
  }

  public Domain getDomainDetails(String domainId) {
    try {
      logger.info("도메인 [{}] 상세 정보 요청 중", domainId);
      User user = getCurrentUser();
      Domain domain = keystoneRepository.getDomainById(user, domainId);
      logger.info("도메인 [{}] 상세 정보 가져오기 성공", domainId);
      return domain;
    } catch (RuntimeException ex) {
      logger.error("도메인 [{}] 상세 정보 가져오기 실패: {}", domainId, ex.getMessage());
      throw ex;
    }
  }

  public Domain updateDomainInfo(Domain domain) {
    try {
      logger.info("도메인 [{}] 정보 업데이트 시도 중", domain.getId());
      User user = getCurrentUser();
      Domain updatedDomain = keystoneRepository.updateDomain(user, domain);
      logger.info("도메인 [{}] 정보 업데이트 성공", domain.getId());
      return updatedDomain;
    } catch (RuntimeException ex) {
      logger.error("도메인 [{}] 정보 업데이트 실패: {}", domain.getId(), ex.getMessage());
      throw ex;
    }
  }

  public List<? extends User> getAllUsers() {
    try {
      logger.info("모든 사용자 목록 요청 중");
      User user = getCurrentUser();
      List<? extends User> users = keystoneRepository.getUsers(user);
      logger.info("모든 사용자 목록 가져오기 성공");
      return users;
    } catch (RuntimeException ex) {
      logger.error("모든 사용자 목록 가져오기 실패: {}", ex.getMessage());
      throw ex;
    }
  }

  public User getUserDetails(String userId) {
    try {
      logger.info("사용자 [{}] 상세 정보 요청 중", userId);
      User user = getCurrentUser();
      User userDetails = keystoneRepository.getUserById(user, userId);
      logger.info("사용자 [{}] 상세 정보 가져오기 성공", userId);
      return userDetails;
    } catch (RuntimeException ex) {
      logger.error("사용자 [{}] 상세 정보 가져오기 실패: {}", userId, ex.getMessage());
      throw ex;
    }
  }

  public Project updateProjectInfo(Project project) {
    try {
      logger.info("프로젝트 업데이트 시도: projectId={}", project.getId());
      User user = getCurrentUser();
      Project updatedProject = keystoneRepository.updateProject(user, project);
      logger.info("프로젝트 업데이트 성공: projectId={}", project.getId());
      return updatedProject;
    } catch (RuntimeException ex) {
      logger.error("프로젝트 업데이트 중 오류 발생: projectId={}, error={}", project.getId(), ex.getMessage());
      throw new RuntimeException("프로젝트 업데이트 중 오류가 발생했습니다.", ex);
    }
  }

  public ProjectInfoDto getCurrentProjectAndAvailableProjects() {
    try {
      logger.info("현재 프로젝트 및 사용 가능한 프로젝트 목록 요청 중");
      User user = getCurrentUser();

      String currentProjectId = user.getDefaultProjectId();
      Project currentProject = keystoneRepository.getProjectById(user, currentProjectId);
      if (currentProject == null) {
        logger.error("현재 프로젝트 ID [{}]가 유효하지 않습니다.", currentProjectId);
        throw new IllegalArgumentException("현재 프로젝트 ID가 유효하지 않습니다.");
      }

      List<? extends Project> availableProjects = keystoneRepository.getProjects(user);

      logger.info("현재 프로젝트 및 사용 가능한 프로젝트 목록 가져오기 성공");
      return ProjectInfoDto.builder()
              .currentProject(currentProject)
              .availableProjects(availableProjects)
              .build();
    } catch (RuntimeException ex) {
      logger.error("현재 프로젝트 및 사용 가능한 프로젝트 목록 가져오기 실패: {}", ex.getMessage());
      throw ex;
    }
  }

  public String updateCurrentProjectId(String projectId) {
    try {
      logger.info("기본 프로젝트 ID [{}]로 업데이트 시도 중", projectId);
      User user = getCurrentUser();

      Project project = keystoneRepository.getProjectById(user, projectId);
      if (project == null) {
        logger.error("프로젝트 ID [{}]가 유효하지 않습니다.", projectId);
        throw new IllegalArgumentException("프로젝트 ID가 유효하지 않습니다.");
      }

      keystoneRepository.updateUser(user.toBuilder()
                      .defaultProjectId(projectId)
                      .build());

      logger.info("기본 프로젝트 ID [{}]로 업데이트 성공", projectId);
      return projectId;
    } catch (RuntimeException ex) {
      logger.error("기본 프로젝트 ID 업데이트 실패: {}", ex.getMessage());
      throw ex;
    }
  }

  private User getCurrentUser() {
    return SecurityUtils.getAuthenticatedUserDetails().getUser();
  }
}
