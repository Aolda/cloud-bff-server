package com.aoldacloud.console.domain.compute.dto;

import lombok.Builder;
import lombok.Getter;
import org.openstack4j.model.compute.Flavor;

@Builder
@Getter
public class ServerFlavorDto {
  private String id;
  private String name;
  private int ram;
  private int vcpus;
  private  int disk;

  public static ServerFlavorDto fromFlavor(Flavor flavor) {
    if(flavor == null) {
      return null;
    }
    return ServerFlavorDto.builder()
            .id(flavor.getId())
            .name(flavor.getName())
            .vcpus(flavor.getVcpus())
            .ram(flavor.getRam())
            .disk(flavor.getDisk())
            .build();
  }

}
