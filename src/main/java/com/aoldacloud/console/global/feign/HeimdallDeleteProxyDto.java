package com.aoldacloud.console.global.feign;

import com.aoldacloud.console.domain.heimdall.dto.ProxyCreateDto;

public class HeimdallDeleteProxyDto {
    private final String username;
    private final Long id;

    public HeimdallDeleteProxyDto(String username, String id) {
        this.username = username;
        this.id = Long.valueOf(id);
    }
}
