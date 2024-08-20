package com.aoldacloud.console.config;

import com.aoldacloud.console.security.entity.CloudSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
  @Value("${spring.data.redis.host:localhost}")
  private String host;
  @Value("${spring.data.redis.port:6379}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  @Bean(name = "cloudSessionRedisTemplate")
  public RedisTemplate<String, CloudSession> cloudSessionRedisTemplate() {
    RedisTemplate<String, CloudSession> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    return redisTemplate;
  }
}