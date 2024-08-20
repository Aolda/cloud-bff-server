package com.aoldacloud.console.domain.heimdall.dto;

import com.aoldacloud.console.domain.heimdall.Proxy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyDetailsDto {
    private String id;
    private String ip;
    private String subDomain;
    private int port;

    public ProxyDetailsDto(Proxy proxy) {
        this.id = proxy.getId();
        this.ip = proxy.getIp();
        this.subDomain = proxy.getSubDomain();
        this.port = proxy.getPort();
    }
}
