package com.aoldacloud.console.domain.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.model.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterDetailsDto {

  private static final Logger logger = LoggerFactory.getLogger(RouterDetailsDto.class);

  private String id;
  private List<? extends HostRoute> routes;
  private boolean isAdminStateUp;
  private State status;
  private ExternalGateway externalGatewayInfo;
  private Boolean distributed;

  public RouterDetailsDto(Router router) {
    this.id = router.getId();
    this.routes = router.getRoutes();
    this.isAdminStateUp = router.isAdminStateUp();
    this.status = router.getStatus();
    this.externalGatewayInfo = router.getExternalGatewayInfo();
    this.distributed = router.getDistributed();
  }
}
