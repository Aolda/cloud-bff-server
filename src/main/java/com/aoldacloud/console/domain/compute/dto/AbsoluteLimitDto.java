package com.aoldacloud.console.domain.compute.dto;

import lombok.Builder;
import lombok.Getter;
import org.openstack4j.model.compute.AbsoluteLimit;

@Getter
@Builder
public class AbsoluteLimitDto {
  private final int maxServerMeta;
  private final int maxPersonality;
  private final int maxImageMeta;
  private final int maxPersonalitySize;
  private final int maxTotalCores;
  private final int maxTotalInstances;
  private final int maxTotalRAMSize;
  private final int totalVolumesUsed;
  private final int maxSecurityGroupRules;
  private final int maxTotalKeypairs;
  private final int totalCoresUsed;
  private final int maxTotalVolumes;
  private final int totalRAMUsed;
  private final int totalInstancesUsed;
  private final int maxSecurityGroups;
  private final int totalVolumeGigabytesUsed;
  private final int totalSecurityGroupsUsed;
  private final int maxTotalFloatingIps;
  private final int totalKeyPairsUsed;
  private final int maxTotalVolumeGigabytes;
  private final int serverMetaUsed;
  private final int personalityUsed;
  private final int imageMetaUsed;
  private final int personalitySizeUsed;
  private final int securityGroupRulesUsed;
  private final int totalFloatingIpsUsed;
  private final int maxServerGroupMembers;
  private final int maxServerGroups;

  // fromAbsoluteLimit 메서드
  public static AbsoluteLimitDto fromAbsoluteLimit(AbsoluteLimit absoluteLimit) {
    return AbsoluteLimitDto.builder()
            .maxServerMeta(absoluteLimit.getMaxServerMeta())
            .maxPersonality(absoluteLimit.getMaxPersonality())
            .maxImageMeta(absoluteLimit.getMaxImageMeta())
            .maxPersonalitySize(absoluteLimit.getMaxPersonalitySize())
            .maxTotalCores(absoluteLimit.getMaxTotalCores())
            .maxTotalInstances(absoluteLimit.getMaxTotalInstances())
            .maxTotalRAMSize(absoluteLimit.getMaxTotalRAMSize())
            .totalVolumesUsed(absoluteLimit.getTotalVolumesUsed())
            .maxSecurityGroupRules(absoluteLimit.getMaxSecurityGroupRules())
            .maxTotalKeypairs(absoluteLimit.getMaxTotalKeypairs())
            .totalCoresUsed(absoluteLimit.getTotalCoresUsed())
            .maxTotalVolumes(absoluteLimit.getMaxTotalVolumes())
            .totalRAMUsed(absoluteLimit.getTotalRAMUsed())
            .totalInstancesUsed(absoluteLimit.getTotalInstancesUsed())
            .maxSecurityGroups(absoluteLimit.getMaxSecurityGroups())
            .totalVolumeGigabytesUsed(absoluteLimit.getTotalVolumeGigabytesUsed())
            .totalSecurityGroupsUsed(absoluteLimit.getTotalSecurityGroupsUsed())
            .maxTotalFloatingIps(absoluteLimit.getMaxTotalFloatingIps())
            .totalKeyPairsUsed(absoluteLimit.getTotalKeyPairsUsed())
            .maxTotalVolumeGigabytes(absoluteLimit.getMaxTotalVolumeGigabytes())
            .serverMetaUsed(absoluteLimit.getServerMetaUsed())
            .personalityUsed(absoluteLimit.getPersonalityUsed())
            .imageMetaUsed(absoluteLimit.getImageMetaUsed())
            .personalitySizeUsed(absoluteLimit.getPersonalitySizeUsed())
            .securityGroupRulesUsed(absoluteLimit.getSecurityGroupRulesUsed())
            .totalFloatingIpsUsed(absoluteLimit.getTotalFloatingIpsUsed())
            .maxServerGroupMembers(absoluteLimit.getMaxServerGroupMembers())
            .maxServerGroups(absoluteLimit.getMaxServerGroups())
            .build();
  }
}
