package com.aoldacloud.console.global.feign;

import com.aoldacloud.console.domain.heimdall.dto.ProxyUpdateDto;

public class HeimdallUpdateProxyDto {
    private final Long id;
    private final String username;
    private final String ip;
    private final String subDomain;
    private final int port;

    public HeimdallUpdateProxyDto(String username, ProxyUpdateDto proxyUpdateDto) {
        this.id = Long.valueOf(proxyUpdateDto.getProxyId());
        this.username = username;
        this.ip = proxyUpdateDto.getIp();
        this.subDomain = proxyUpdateDto.getSubDomain();
        this.port = proxyUpdateDto.getPort();
    }
}
