package com.aoldacloud.console.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.aoldacloud.console"})
public class FeignConfig {

}