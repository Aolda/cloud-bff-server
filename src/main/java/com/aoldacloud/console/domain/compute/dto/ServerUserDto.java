package com.aoldacloud.console.domain.compute.dto;

import lombok.Builder;
import lombok.Getter;
import org.openstack4j.model.identity.v3.User;

@Getter
@Builder
public class ServerUserDto {
  String id;
  String name;

  public static ServerUserDto fromUser(User user) {
    if(user == null) {
      return null;
    }
    return ServerUserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .build();
  }
}
