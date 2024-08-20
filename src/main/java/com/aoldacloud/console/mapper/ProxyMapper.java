package com.aoldacloud.console.mapper;

import com.aoldacloud.console.domain.heimdall.Proxy;
import com.aoldacloud.console.global.feign.HeimdallProxyDto;

import java.util.List;
import java.util.stream.Collectors;

public class ProxyMapper {
    public static Proxy dtoToEntity(HeimdallProxyDto heimdallProxyDto) {
        return Proxy.builder()
                .id(String.valueOf(heimdallProxyDto.getId()))
                .subDomain(heimdallProxyDto.getSubDomain())
                .ip(heimdallProxyDto.getIp())
                .port(heimdallProxyDto.getPort())
                .build();
    }

    public static List<Proxy> dtosToEntities(List<HeimdallProxyDto> heimdallProxyDtos) {
        return heimdallProxyDtos.stream().map(proxy -> Proxy.builder()
                .id(String.valueOf(proxy.getId()))
                .ip(proxy.getIp())
                .subDomain(proxy.getSubDomain())
                .port(proxy.getPort())
                .build()).collect(Collectors.toList());
    }
}
