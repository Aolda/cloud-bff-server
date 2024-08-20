package com.aoldacloud.console.domain.heimdall;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Proxy {
    private String id;
    private String ip;
    private String subDomain;
    private int port;
}
