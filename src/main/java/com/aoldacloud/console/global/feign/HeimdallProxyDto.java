package com.aoldacloud.console.global.feign;

import lombok.Data;

@Data
public class HeimdallProxyDto {
    private final Long id;
    private final String ip;
    private final String subDomain;
    private final int port;
}
