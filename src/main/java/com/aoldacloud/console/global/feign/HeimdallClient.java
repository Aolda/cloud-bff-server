package com.aoldacloud.console.global.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "heimdallClient", url = "https://heimdall.aolda.in")
public interface HeimdallClient {

    @PostMapping("/add-proxy")
    HeimdallProxyDto addProxy(@RequestBody HeimdallCreateProxyDto heimdallCreateProxyDto);

    @PutMapping("update-proxy")
    HeimdallProxyDto updateProxy(@RequestBody HeimdallUpdateProxyDto heimdallUpdateProxyDto);

    @DeleteMapping("delete-proxy")
    HeimdallBasicDto deleteProxy(@RequestBody HeimdallDeleteProxyDto heimdallDeleteProxyDto);

    @GetMapping("/read-proxy/{proxyId}")
    HeimdallProxyDto readProxy(@PathVariable(name="proxyId") Long proxyId);

    @GetMapping("/read-proxy-list/{username}")
    List<HeimdallProxyDto> readProxyList(@PathVariable(name="username") String username);
}