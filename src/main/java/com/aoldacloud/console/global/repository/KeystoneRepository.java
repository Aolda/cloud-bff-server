package com.aoldacloud.console.global.repository;

import com.aoldacloud.console.domain.auth.dto.UserDto;
import com.aoldacloud.console.global.OpenstackService;
import com.aoldacloud.console.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class KeystoneRepository {

  private final RedisTemplate<String, User> userRedisTemplate;

  /**
   * 로그인한 사용자의 정보를 토대로 토큰을 생성하고, 해당 토큰을 Redis에 캐싱합니다.
   *
   * @param username 사용자 이름
   * @param password 사용자 패스워드
   * @return 사용자 정보와 생성된 토큰을 포함한 UserDto
   */
  public UserDto getUserByCredentials(String username, String password) {
    try {
      OSClientV3 client = OpenstackService.getClient(username,password);

      User user = client.getToken().getUser();
      if (user.getDefaultProjectId() == null) {
        List<? extends  Project> projects = client.identity().projects().list();
        if (!projects.isEmpty()) {
          Project project = projects.getFirst();
          user.toBuilder().defaultProjectId(project.getId()).build();
        }
      }

      String token = SecurityUtils.generateToken(user.getId());
      user.toBuilder().password(password).build();
      userRedisTemplate.opsForValue().set(token, user, 3, TimeUnit.HOURS);

      user.toBuilder().password(null).build();
      return UserDto.builder()
              .user(user)
              .authToken(token)
              .build();
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      throw new RuntimeException("Keystone에서 사용자 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Project> getProjects(User user) {
    try {
      return OpenstackService.getClient(user)
              .identity().projects()
              .list();

    } catch (Exception ex) {
      throw new RuntimeException("프로젝트 목록을 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Project getProjectById(User user, String projectId) {
    try {
      return OpenstackService.getClient(user).identity().projects().get(projectId);
    } catch (Exception ex) {
      throw new RuntimeException("프로젝트 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Project updateProject(User user, Project project) {
    try {
      Project updatedProject = OpenstackService.getClient(user)
              .identity().projects()
              .update(project);

      userRedisTemplate.opsForValue().set(user.getId(), user);
      return updatedProject;
    } catch (Exception ex) {
      throw new RuntimeException("프로젝트 정보를 업데이트하는 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Domain> getDomains(User user) {
    try {
      return OpenstackService.getClient(user)
              .identity().domains()
              .list();

    } catch (Exception ex) {
      throw new RuntimeException("도메인 목록을 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Domain getDomainById(User user, String domainId) {
    try {
      return OpenstackService.getClient(user)
              .identity().domains()
              .get(domainId);

    } catch (Exception ex) {
      throw new RuntimeException("도메인 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Domain updateDomain(User user, Domain domain) {
    try {
      Domain updatedDomain = OpenstackService.getClient(user)
              .identity().domains()
              .update(domain);

      userRedisTemplate.opsForValue().set(user.getId(), user); // 업데이트 후 사용자 정보를 다시 캐싱
      return updatedDomain;
    } catch (Exception ex) {
      throw new RuntimeException("도메인 정보를 업데이트하는 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends User> getUsers(User user) {
    try {
      return OpenstackService.getClient(user)
              .identity().users()
              .list();

    } catch (Exception ex) {
      throw new RuntimeException("사용자 목록을 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public User getUserById(User user, String userId) {
    try {
      return OpenstackService.getClient(user)
              .identity().users()
              .get(userId);

    } catch (Exception ex) {
      throw new RuntimeException("사용자 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public UserDto updateUser(User updatedUser) {
    try {
      User user = SecurityUtils.getAuthenticatedUserDetails().getUser();
      User updatedUserEntity = OpenstackService.getClient(user)
              .identity().users()
              .update(updatedUser);

      userRedisTemplate.opsForValue().set(user.getId(), updatedUserEntity);
      return UserDto.builder()
              .user(updatedUserEntity)
              .authToken(user.getId())
              .build();

    } catch (Exception ex) {
      throw new RuntimeException("사용자 정보를 업데이트하는 중 오류가 발생했습니다.", ex);
    }
  }
}
