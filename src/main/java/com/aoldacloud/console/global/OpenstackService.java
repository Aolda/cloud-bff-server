package com.aoldacloud.console.global;

import com.aoldacloud.console.util.SecurityUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.openstack.OSFactory;

@Getter
@Slf4j
public enum OpenstackService {
  Keystone("https://keystone.aoldacloud.com/v3");

  private final String endpoint;
  OpenstackService(String url) {
    this.endpoint = url;
  }

  public static OSClient.OSClientV3 getClient() {
    Token token = SecurityUtils.getAuthenticatedUserDetails().getCloudSession().getToken();
    OSClient.OSClientV3 client = OSFactory.builderV3()
            .token(token.getId())
            .scopeToProject(Identifier.byId(token.getProject().getId()), Identifier.byName("default"))
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .authenticate();
    return client;
  }

  public static OSClient.OSClientV3 getClient(String username, String password) {
    return OSFactory.builderV3()
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .credentials(username, password, Identifier.byName("default"))
            .authenticate();
  }


}
