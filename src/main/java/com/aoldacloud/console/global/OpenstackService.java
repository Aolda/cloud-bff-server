package com.aoldacloud.console.global;

import com.aoldacloud.console.util.SecurityUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.openstack.OSFactory;

@Getter
@Slf4j
public enum OpenstackService {
  Keystone("https://keystone.aoldacloud.com/v3"),
  KeystonV2("https://keystone.aoldacloud.com/v2");

  private final String endpoint;
  OpenstackService(String url) {
    this.endpoint = url;
  }

  public static OSClient.OSClientV3 getClient() {
    try {
      Token token = SecurityUtils.getAuthenticatedUserDetails().getCloudSession().getToken();
     return OSFactory.builderV3()
              .token(token.getId())
              .scopeToProject(Identifier.byId(token.getProject().getId()), Identifier.byName("default"))
              .endpoint(OpenstackService.Keystone.getEndpoint())
              .authenticate();
    }
    catch (Exception e) {
      throw new UnauthorizedException("클라우드 인증에 실패하였습니다.");
    }
  }

  public static OSClient.OSClientV3 getClient(String username, String password) {
    return OSFactory.builderV3()
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .credentials(username, password, Identifier.byName("default"))
            .authenticate();
  }

  public static OSClient.OSClientV3 getClient(String username, String password, Identifier project) {
    return OSFactory.builderV3()
            .endpoint(OpenstackService.Keystone.getEndpoint())
            .credentials(username, password, Identifier.byName("default"))
            .scopeToProject(project)
            .authenticate();
  }


}
