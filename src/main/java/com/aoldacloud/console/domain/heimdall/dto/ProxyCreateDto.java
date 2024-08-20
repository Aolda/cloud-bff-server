package com.aoldacloud.console.domain.heimdall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyCreateDto {
    private String ip;
    private String subDomain;
    private int port;
}
