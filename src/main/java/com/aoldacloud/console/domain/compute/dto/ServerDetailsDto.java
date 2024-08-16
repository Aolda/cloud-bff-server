package com.aoldacloud.console.domain.compute.dto;

import com.aoldacloud.console.global.OpenstackService;
import com.aoldacloud.console.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.SecurityGroup;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v3.Authentication;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.openstack.client.OSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerDetailsDto {

  private static final Logger logger = LoggerFactory.getLogger(ServerDetailsDto.class);

  private String id;
  private String name;
  private String fixedIp;
  private String floatingIp;
  private String status;
  private int progress;
  private SecurityGroup securityGroup;
  private ServerUserDto user;
  private ServerFlavorDto flavor;
  private Image image;
  private List<Volume> volumes;

  public static ServerDetailsDto fromServer(Server server) {
    logger.debug("Entering fromServer method with server: {}", server);

    String fixedIp = null;
    String floatingIp = null;
    List<Volume> volumes = new ArrayList<>();
    Image image = null;

    var client = OpenstackService.getClient();

    try {
      if (server.getAddresses() != null) {
        String key = server.getAddresses().getAddresses().keySet().stream().findFirst().orElse(null);
        if (key != null) {
          var networkDetails = server.getAddresses().getAddresses().get(key);
          for (var ip : networkDetails) {
            if ("fixed".equalsIgnoreCase(ip.getType())) {
              fixedIp = ip.getAddr();
            } else if ("floating".equalsIgnoreCase(ip.getType())) {
              floatingIp = ip.getAddr();
            }
          }
        }
      }

      logger.debug("Processed IP addresses - fixedIp: {}, floatingIp: {}", fixedIp, floatingIp);

      // 볼륨 및 이미지 정보 처리
      if (server.getOsExtendedVolumesAttached() != null) {
        volumes = server.getOsExtendedVolumesAttached().stream()
                .map(attachedVolume -> {
                  try {
                    return client.blockStorage().volumes().get(attachedVolume);
                  } catch (Exception e) {
                    logger.error("Failed to get volume for attachedVolume: {} {}", attachedVolume, e.toString());
                    return null;
                  }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Optional<Image> firstImage = volumes.stream()
                .map(Volume::getImageRef)
                .filter(Objects::nonNull)
                .map(imageRef -> {
                  try {
                    return client.imagesV2().get(imageRef);
                  } catch (Exception e) {
                    logger.error("Failed to get image for imageRef: {}", imageRef);
                    return null;
                  }
                })
                .filter(Objects::nonNull)
                .findFirst();

        if (firstImage.isPresent()) {
          image = firstImage.get();
        }
      }

      logger.debug("Processed volumes and image - volumes: {}, image: {}", volumes, image);

      SecurityGroup securityGroup = server.getSecurityGroups() != null && !server.getSecurityGroups().isEmpty()
              ? server.getSecurityGroups().getFirst() : null;

      Flavor flavor = server.getFlavor();
      User user = null;
      try {
        user = client.identity().users().get(server.getUserId());
      } catch (Exception e) {
        logger.error("Failed to get user for userId: {}", server.getUserId(), e);
      }

      logger.debug("Processed securityGroup, flavor, and user - securityGroup: {}, flavor: {}, user: {}",
              securityGroup, flavor, user);

      ServerDetailsDto dto = ServerDetailsDto.builder()
              .id(server.getId())
              .name(server.getName())
              .fixedIp(fixedIp)
              .floatingIp(floatingIp)
              .status(server.getStatus().value())
              .progress(server.getProgress())
              .securityGroup(securityGroup)
              .user(ServerUserDto.fromUser(user))
              .flavor(ServerFlavorDto.fromFlavor(flavor))
              .image(image)
              .volumes(volumes)
              .build();

      logger.debug("Exiting fromServer method with ServerDetailsDto: {}", dto);
      return dto;

    } catch (Exception e) {
      logger.error("An error occurred while transforming Server to ServerDetailsDto", e);
      throw new RuntimeException("Failed to transform Server to ServerDetailsDto", e);
    }
  }
}
