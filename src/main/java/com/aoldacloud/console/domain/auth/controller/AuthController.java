package com.aoldacloud.console.domain.auth.controller;

import com.aoldacloud.console.domain.auth.dto.LoginDto;
import com.aoldacloud.console.domain.auth.dto.ProjectInfoDto;
import com.aoldacloud.console.domain.auth.dto.UserDto;
import com.aoldacloud.console.global.ResponseWrapper;
import com.aoldacloud.console.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "AoldaCloud 인증, 프로젝트, 도메인 및 사용자 관리와 관련된 API")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final AuthService authService;

  @Operation(summary = "자격 증명을 사용한 로그인", description = "사용자 이름과 비밀번호를 사용하여 로그인합니다.", security = @SecurityRequirement(name = "AoldaCloudAuth"))
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "로그인 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
          @ApiResponse(responseCode = "401", description = "로그인 실패, 자격 증명 오류",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/login")
  public ResponseEntity<ResponseWrapper<UserDto>> loginWithCredentials(@RequestBody LoginDto loginDto, HttpServletResponse response) {
    try {
      UserDto userDto = authService.loginWithCredentials(loginDto);

      ResponseCookie cookie = ResponseCookie.from("X-AUTH-TOKEN", userDto.getAuthToken())
              .path("/")
              .sameSite("None")
              .httpOnly(false)
              .secure(false)
              .maxAge(3 * 60 * 60)
              .build();

      response.addHeader("Set-Cookie", cookie.toString());

      return ResponseWrapper.success(userDto);
    } catch (RuntimeException ex) {
      logger.error("ID/Password 로그인 실패: {}", ex.getMessage());
      return ResponseWrapper.error("로그인 실패", HttpStatus.UNAUTHORIZED);
    }
  }

  @Operation(summary = "프로젝트 목록 가져오기", description = "인증된 사용자가 접근할 수 있는 프로젝트 목록을 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "프로젝트 목록 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
          @ApiResponse(responseCode = "500", description = "프로젝트 목록을 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/projects")
  public ResponseEntity<ResponseWrapper<List<? extends Project>>> getProjects() {
    List<? extends Project> projects = authService.getUserProjects();
    return ResponseWrapper.success(projects);
  }

  @Operation(summary = "프로젝트 상세 정보 가져오기", description = "특정 프로젝트 ID로 프로젝트의 상세 정보를 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "프로젝트 상세 정보 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
          @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "프로젝트 상세 정보를 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/projects/{projectId}")
  public ResponseEntity<ResponseWrapper<Project>> getProjectDetails(@PathVariable String projectId) {
    logger.info("프로젝트 상세 정보 요청: projectId={}", projectId);
    Project project = authService.getProjectDetails(projectId);
    if (project == null) {
      return ResponseWrapper.error("프로젝트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
    return ResponseWrapper.success(project);
  }

  @Operation(summary = "프로젝트 정보 업데이트", description = "특정 프로젝트의 세부 정보를 업데이트합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "프로젝트 업데이트 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
          @ApiResponse(responseCode = "500", description = "프로젝트 업데이트 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/projects")
  public ResponseEntity<ResponseWrapper<Project>> updateProjectInfo(@RequestBody Project project) {
    logger.info("프로젝트 수정 요청: projectId={}", project.getId());
    Project updatedProject = authService.updateProjectInfo(project);
    return ResponseWrapper.success(updatedProject);
  }

  @Operation(summary = "도메인 목록 가져오기", description = "인증된 사용자가 접근할 수 있는 도메인 목록을 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "도메인 목록 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Domain.class))),
          @ApiResponse(responseCode = "500", description = "도메인 목록을 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/domains")
  public ResponseEntity<ResponseWrapper<List<? extends Domain>>> getDomains() {
    logger.info("도메인 목록 요청 시작");
    List<? extends Domain> domains = authService.getUserDomains();
    return ResponseWrapper.success(domains);
  }

  @Operation(summary = "도메인 상세 정보 가져오기", description = "특정 도메인 ID로 도메인의 상세 정보를 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "도메인 상세 정보 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Domain.class))),
          @ApiResponse(responseCode = "404", description = "도메인을 찾을 수 없음",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "도메인 상세 정보를 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/domains/{domainId}")
  public ResponseEntity<ResponseWrapper<Domain>> getDomainDetails(@PathVariable String domainId) {
    logger.info("도메인 상세 정보 요청: domainId={}", domainId);
    Domain domain = authService.getDomainDetails(domainId);
    if (domain == null) {
      return ResponseWrapper.error("도메인을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
    return ResponseWrapper.success(domain);
  }

  @Operation(summary = "도메인 정보 업데이트", description = "특정 도메인의 세부 정보를 업데이트합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "도메인 업데이트 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Domain.class))),
          @ApiResponse(responseCode = "500", description = "도메인 업데이트 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/domains")
  public ResponseEntity<ResponseWrapper<Domain>> updateDomainInfo(@RequestBody Domain domain) {
    logger.info("도메인 수정 요청: domainId={}", domain.getId());
    Domain updatedDomain = authService.updateDomainInfo(domain);
    return ResponseWrapper.success(updatedDomain);
  }

  @Operation(summary = "사용자 목록 가져오기", description = "인증된 사용자가 접근할 수 있는 사용자 목록을 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "사용자 목록 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
          @ApiResponse(responseCode = "500", description = "사용자 목록을 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/users")
  public ResponseEntity<ResponseWrapper<List<? extends User>>> getUsers() {
    logger.info("사용자 목록 요청 시작");
    List<? extends User> users = authService.getAllUsers();
    return ResponseWrapper.success(users);
  }

  @Operation(summary = "사용자 상세 정보 가져오기", description = "특정 사용자 ID로 사용자의 상세 정보를 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "사용자 상세 정보 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
          @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "사용자 상세 정보를 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseWrapper<User>> getUserDetails(@PathVariable String userId) {
    logger.info("사용자 상세 정보 요청: userId={}", userId);
    User user = authService.getUserDetails(userId);
    if (user == null) {
      return ResponseWrapper.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
    return ResponseWrapper.success(user);
  }

  @Operation(summary = "사용자 정보 업데이트", description = "특정 사용자의 세부 정보를 업데이트합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "사용자 업데이트 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
          @ApiResponse(responseCode = "500", description = "사용자 업데이트 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/users")
  public ResponseEntity<ResponseWrapper<UserDto>> updateUserInfo(@RequestBody User user) {
    logger.info("사용자 수정 요청: userId={}", user.getId());
    UserDto updatedUser = authService.updateUserInfo(user);
    return ResponseWrapper.success(updatedUser);
  }

  @Operation(summary = "현재 프로젝트 및 사용 가능한 프로젝트 목록 가져오기", description = "현재 인증된 사용자의 현재 프로젝트 ID와 사용 가능한 프로젝트 목록을 반환합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "프로젝트 정보 반환",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectInfoDto.class))),
          @ApiResponse(responseCode = "500", description = "프로젝트 정보를 가져오는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/projects/current")
  public ResponseEntity<ResponseWrapper<ProjectInfoDto>> getCurrentProjectAndAvailableProjects() {
    ProjectInfoDto projectInfo = authService.getCurrentProjectAndAvailableProjects();
    return ResponseWrapper.success(projectInfo);
  }

  @Operation(summary = "현재 프로젝트 업데이트", description = "현재 인증된 사용자의 기본 프로젝트 ID를 업데이트합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "기본 프로젝트 업데이트 성공",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "400", description = "유효하지 않은 프로젝트 ID",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "기본 프로젝트를 업데이트하는 중 오류 발생",
                  content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/projects/current")
  public ResponseEntity<ResponseWrapper<String>> updateCurrentProject(@RequestParam String projectId) {
    String updatedProjectId = authService.updateCurrentProjectId(projectId);
    return ResponseWrapper.success(updatedProjectId);
  }
}
