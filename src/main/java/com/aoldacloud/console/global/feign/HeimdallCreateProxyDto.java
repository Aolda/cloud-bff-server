package com.aoldacloud.console.global.feign;

import com.aoldacloud.console.domain.heimdall.dto.ProxyCreateDto;

public class HeimdallCreateProxyDto {
    private final String username;
    private final String ip;
    private final String subDomain;
    private final int port;

    public HeimdallCreateProxyDto(String username, ProxyCreateDto proxyCreateDto) {
        this.username = username;
        this.ip = proxyCreateDto.getIp();
        this.subDomain = proxyCreateDto.getSubDomain();
        this.port = proxyCreateDto.getPort();
    }
}
