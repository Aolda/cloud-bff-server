package com.aoldacloud.console.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 내장 Redis 서버를 시작하고 중지하는 설정 클래스입니다.
 * 이 클래스는 "local" 또는 "embedded-test" 프로파일에서만 활성화됩니다.
 */
@Slf4j
@Profile({"local", "embedded-test"})
@Configuration
public class EmbeddedRedisConfig {

  /**
   * Redis 서버가 실행될 포트입니다. 기본값은 6379입니다.
   */
  @Value("${spring.data.redis.port:6379}")
  private int redisPort;

  /**
   * 내장 Redis 서버 인스턴스입니다.
   */
  private RedisServer redisServer;

  /**
   * 애플리케이션 시작 시 내장 Redis 서버를 시작합니다.
   * Redis 서버가 이미 실행 중인 경우, 사용 가능한 포트를 찾아 Redis 서버를 시작합니다.
   *
   * @throws IOException 입출력 예외가 발생할 수 있습니다.
   */
  @PostConstruct
  private void start() throws IOException {
    log.info("내장 Redis 서버를 시작합니다.");
    int port = isRedisRunning() ? findAvailablePort() : redisPort;
    redisServer = new RedisServer(port);
    redisServer.start();
    log.info("내장 Redis 서버가 포트 {}에서 시작되었습니다.", port);
  }

  /**
   * 애플리케이션 종료 시 내장 Redis 서버를 중지합니다.
   *
   * @throws IOException 입출력 예외가 발생할 수 있습니다.
   */
  @PreDestroy
  private void stop() throws IOException {
    log.info("내장 Redis 서버를 중지합니다.");
    if (redisServer != null) {
      redisServer.stop();
      log.info("내장 Redis 서버가 중지되었습니다.");
    } else {
      log.warn("내장 Redis 서버가 실행 중이지 않아 중지할 수 없습니다.");
    }
  }

  /**
   * Redis 서버가 현재 실행 중인지 확인합니다.
   *
   * @return Redis 서버가 실행 중이면 true, 그렇지 않으면 false를 반환합니다.
   * @throws IOException 입출력 예외가 발생할 수 있습니다.
   */
  private boolean isRedisRunning() throws IOException {
    log.info("Redis 서버가 실행 중인지 확인합니다.");
    boolean running = isRunning(executeGrepProcessCommand(redisPort));
    if (running) {
      log.info("Redis 서버가 현재 포트 {}에서 실행 중입니다.", redisPort);
    } else {
      log.info("Redis 서버가 현재 실행 중이지 않습니다.");
    }
    return running;
  }

  /**
   * 사용 가능한 포트를 찾습니다. 10000번부터 65535번 포트까지 확인합니다.
   *
   * @return 사용 가능한 포트를 반환합니다.
   * @throws IOException 입출력 예외가 발생할 수 있습니다.
   */
  public int findAvailablePort() throws IOException {
    log.info("사용 가능한 포트를 찾습니다.");
    for (int port = 10000; port <= 65535; port++) {
      Process process = executeGrepProcessCommand(port);
      if (!isRunning(process)) {
        log.info("사용 가능한 포트를 찾았습니다: {}", port);
        return port;
      }
    }
    throw new IllegalArgumentException("사용 가능한 포트를 찾을 수 없습니다: 10000 ~ 65535");
  }

  /**
   * 지정된 포트에 대해 netstat 명령어를 실행하여 해당 포트가 사용 중인지 확인합니다.
   *
   * @param port 확인할 포트 번호
   * @return netstat 명령어 결과를 포함하는 Process 객체
   * @throws IOException 입출력 예외가 발생할 수 있습니다.
   */
  private Process executeGrepProcessCommand(int port) throws IOException {
    String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
    String[] shell = {"/bin/sh", "-c", command};
    log.info("포트 {}에서 netstat 명령어를 실행합니다: {}", port, command);
    return Runtime.getRuntime().exec(shell);
  }

  /**
   * Process의 실행 결과를 바탕으로 해당 프로세스가 실행 중인지 확인합니다.
   *
   * @param process 확인할 Process 객체
   * @return 프로세스가 실행 중이면 true, 그렇지 않으면 false를 반환합니다.
   */
  private boolean isRunning(Process process) {
    StringBuilder pidInfo = new StringBuilder();
    try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = input.readLine()) != null) {
        pidInfo.append(line);
      }
      log.info("프로세스 출력: {}", pidInfo.toString());
    } catch (Exception e) {
      log.error("프로세스 실행 중 오류가 발생했습니다: {}", e.getMessage());
    }
    boolean isRunning = !pidInfo.isEmpty();
    log.info("프로세스 실행 상태: {}", isRunning ? "실행 중" : "실행 중지");
    return isRunning;
  }
}
