package com.aoldacloud.console.domain.auth.repository;

import com.aoldacloud.console.domain.auth.dto.UserDto;
import com.aoldacloud.console.global.OpenstackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.openstack.OSFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class KeystoneRepository {

  private final RedisTemplate<String, User> userRedisTemplate;

  private OSClientV3 getClient(User user) {
    return OSFactory.builderV3()
            .credentials(user.getName(), user.getPassword(), Identifier.byName(user.getDomain().getId()))
            .scopeToProject(Identifier.byId(user.getDefaultProjectId()), Identifier.byId(user.getDomain().getId()))
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .authenticate();
  }

  /**
   * 사용자 ID와 현재 시간을 기반으로 해시된 토큰을 생성합니다.
   *
   * @param userId 사용자 ID
   * @return 생성된 토큰
   */
  private String generateToken(String userId) {
    try {
      String data = userId + LocalDateTime.now();
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
      String base64Token = Base64.getUrlEncoder().encodeToString(hash);

      StringBuilder tokenBuilder = new StringBuilder(base64Token);
      while (tokenBuilder.length() < 70) {
        tokenBuilder.append(base64Token);
      }

      return tokenBuilder.substring(0, 70);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("토큰 생성 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 로그인한 사용자의 정보를 토대로 토큰을 생성하고, 해당 토큰을 Redis에 캐싱합니다.
   *
   * @param username 사용자 이름
   * @param password 사용자 패스워드
   * @return 사용자 정보와 생성된 토큰을 포함한 UserDto
   */
  public UserDto getUserByCredentials(String username, String password) {
    try {
      OSClientV3 client = OSFactory.builderV3()
              .endpoint(OpenstackService.Keystone.getEndpoint())
              .credentials(username, password, Identifier.byName("default"))
              .authenticate();

      User user = client.getToken().getUser();
      if (user.getDefaultProjectId() == null) {
        List<? extends  Project> projects = client.identity().projects().list();
        if (!projects.isEmpty()) {
          Project project = projects.getFirst();
          user.toBuilder().defaultProjectId(project.getId()).build();
        }
      }

      String token = generateToken(user.getId());
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
      return getClient(user).identity().projects().list();
    } catch (Exception ex) {
      throw new RuntimeException("프로젝트 목록을 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Project getProjectById(User user, String projectId) {
    try {
      return getClient(user).identity().projects().get(projectId);
    } catch (Exception ex) {
      throw new RuntimeException("프로젝트 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Project updateProject(User user, Project project) {
    try {
      Project updatedProject = getClient(user).identity().projects().update(project);
      userRedisTemplate.opsForValue().set(user.getId(), user); // 업데이트 후 사용자 정보를 다시 캐싱
      return updatedProject;
    } catch (Exception ex) {
      throw new RuntimeException("프로젝트 정보를 업데이트하는 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends Domain> getDomains(User user) {
    try {
      return getClient(user).identity().domains().list();
    } catch (Exception ex) {
      throw new RuntimeException("도메인 목록을 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Domain getDomainById(User user, String domainId) {
    try {
      return getClient(user).identity().domains().get(domainId);
    } catch (Exception ex) {
      throw new RuntimeException("도메인 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public Domain updateDomain(User user, Domain domain) {
    try {
      Domain updatedDomain = getClient(user).identity().domains().update(domain);
      userRedisTemplate.opsForValue().set(user.getId(), user); // 업데이트 후 사용자 정보를 다시 캐싱
      return updatedDomain;
    } catch (Exception ex) {
      throw new RuntimeException("도메인 정보를 업데이트하는 중 오류가 발생했습니다.", ex);
    }
  }

  public List<? extends User> getUsers(User user) {
    try {
      return getClient(user).identity().users().list();
    } catch (Exception ex) {
      throw new RuntimeException("사용자 목록을 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public User getUserById(User user, String userId) {
    try {
      return getClient(user).identity().users().get(userId);
    } catch (Exception ex) {
      throw new RuntimeException("사용자 정보를 가져오는 중 오류가 발생했습니다.", ex);
    }
  }

  public UserDto updateUser(User user, User updatedUser) {
    try {
      User updatedUserEntity = getClient(user).identity().users().update(updatedUser);
      userRedisTemplate.opsForValue().set(user.getId(), updatedUserEntity); // 업데이트 후 사용자 정보를 다시 캐싱
      return UserDto.builder()
              .user(updatedUserEntity)
              .authToken(user.getId())
              .build();
    } catch (Exception ex) {
      throw new RuntimeException("사용자 정보를 업데이트하는 중 오류가 발생했습니다.", ex);
    }
  }
}
