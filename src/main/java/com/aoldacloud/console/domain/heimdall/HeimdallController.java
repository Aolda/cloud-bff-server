package com.aoldacloud.console.domain.heimdall;

import com.aoldacloud.console.domain.heimdall.dto.ProxyCreateDto;
import com.aoldacloud.console.domain.heimdall.dto.ProxyDetailsDto;
import com.aoldacloud.console.domain.heimdall.dto.ProxyUpdateDto;
import com.aoldacloud.console.domain.network.dto.*;
import com.aoldacloud.console.global.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/heimdall")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Heimdall", description = "AoldaCloud Heimdall과 관련된 API")
public class HeimdallController {

    private final HeimdallService heimdallService;

    @Operation(summary = "프록시 생성", description = "새로운 프록시를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프록시 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/heimdall")
    public ResponseEntity<ResponseWrapper<ProxyDetailsDto>> createPort(@RequestBody ProxyCreateDto proxyCreateDto) {
        ProxyDetailsDto createdProxy = heimdallService.createProxy(proxyCreateDto);
        return ResponseWrapper.created(createdProxy);
    }

    @Operation(summary = "프록시 업데이트", description = "기존 프록시를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프록시 업데이트 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/heimdall")
    public ResponseEntity<ResponseWrapper<ProxyDetailsDto>> updatePort(@RequestBody ProxyUpdateDto proxyUpdateDto) {
        ProxyDetailsDto updatedProxy = heimdallService.updateProxy(proxyUpdateDto);
        return ResponseWrapper.success(updatedProxy);
    }

    @Operation(summary = "프록시 삭제", description = "프록시를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "프록시 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/heimdall/{proxyId}")
    public ResponseEntity<Void> deletePort(@PathVariable String proxyId) {
        heimdallService.deleteProxy(proxyId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프록시 목록 조회", description = "프록시 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프록시 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/heimdall")
    public ResponseEntity<ResponseWrapper<List<ProxyDetailsDto>>> listPorts() {
        List<ProxyDetailsDto> proxies = heimdallService.listProxies();
        return ResponseWrapper.success(proxies);
    }

    @Operation(summary = "프록시 상세 정보 조회", description = "특정 프록시의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프록시 상세 정보 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "404", description = "프록시를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/heimdall/{username}")
    public ResponseEntity<ResponseWrapper<ProxyDetailsDto>> getPortDetails(@PathVariable String username) {
        ProxyDetailsDto proxy = heimdallService.getProxyDetails(username);
        if (proxy == null) {
            return ResponseWrapper.error("프록시를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        return ResponseWrapper.success(proxy);
    }
}
