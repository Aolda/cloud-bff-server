package com.aoldacloud.console.global;

import lombok.Getter;

@Getter
public enum OpenstackService {
  Keystone("https://keystone.aoldacloud.com/v3"),
  Nova("https://nova.aoldacloud.com/v2.1"),
  Neutron("https://neutron.aoldacloud.com/v2");

  private final String endpoint;
  OpenstackService(String url) {
    this.endpoint = url;
  }
}
