package com.aoldacloud.console.global;

import lombok.Getter;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.openstack.OSFactory;

@Getter
public enum OpenstackService {
  Keystone("https://keystone.aoldacloud.com/v3");

  private final String endpoint;
  OpenstackService(String url) {
    this.endpoint = url;
  }

  public static OSClient.OSClientV3 getClient(User user) {
    return OSFactory.builderV3()
            .credentials(user.getName(), user.getPassword(), Identifier.byName(user.getDomain().getId()))
            .scopeToProject(Identifier.byId(user.getDefaultProjectId()), Identifier.byId(user.getDomain().getId()))
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .authenticate();
  }

  public static OSClient.OSClientV3 getClient(String username, String password) {
    return OSFactory.builderV3()
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .credentials(username, password, Identifier.byName("default"))
            .authenticate();
  }


}
