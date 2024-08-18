package com.aoldacloud.console.domain.network;

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
@RequestMapping("/api/v1.0/network")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Network", description = "AoldaCloud 네트워크와 관련된 API")
public class NetworkController {

    private final NetworkService networkService;


    // Subnet
    @Operation(summary = "서브넷 생성", description = "새로운 서브넷을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "서브넷 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubnetDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/subnets")
    public ResponseEntity<ResponseWrapper<SubnetDetailsDto>> createSubnet(@RequestBody SubnetCreateDto subnetCreateDto) {
        SubnetDetailsDto createdSubnet = networkService.createSubnet(subnetCreateDto);
        return ResponseWrapper.created(createdSubnet);
    }

    @Operation(summary = "서브넷 삭제", description = "서브넷을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "서브넷 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/subnets/{subnetId}")
    public ResponseEntity<Void> deleteSubnet(@PathVariable String subnetId) {
        networkService.deleteSubnet(subnetId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "서브넷 목록 조회", description = "서브넷 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서브넷 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubnetDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/subnets")
    public ResponseEntity<ResponseWrapper<List<SubnetDetailsDto>>> listSubnets() {
        List<SubnetDetailsDto> subnets = networkService.listSubnets();
        return ResponseWrapper.success(subnets);
    }

    @Operation(summary = "서브넷 상세 정보 조회", description = "특정 서브넷의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서브넷 상세 정보 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubnetDetailsDto.class))),
            @ApiResponse(responseCode = "404", description = "서브넷을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/subnets/{subnetId}")
    public ResponseEntity<ResponseWrapper<SubnetDetailsDto>> getSubnetDetails(@PathVariable String subnetId) {
        SubnetDetailsDto subnet = networkService.getSubnetDetails(subnetId);
        if (subnet == null) {
            return ResponseWrapper.error("서브넷을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        return ResponseWrapper.success(subnet);
    }


    // Port
    @Operation(summary = "포트 생성", description = "새로운 포트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "포트 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/ports")
    public ResponseEntity<ResponseWrapper<PortDetailsDto>> createPort(@RequestBody PortCreateDto portCreateDto) {
        PortDetailsDto createdPort = networkService.createPort(portCreateDto);
        return ResponseWrapper.created(createdPort);
    }

    @Operation(summary = "포트 업데이트", description = "기존 포트를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포트 업데이트 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/ports")
    public ResponseEntity<ResponseWrapper<PortDetailsDto>> updatePort(@RequestBody PortUpdateDto portUpdateDto) {
        PortDetailsDto updatedPort = networkService.updatePort(portUpdateDto);
        return ResponseWrapper.success(updatedPort);
    }

    @Operation(summary = "포트 삭제", description = "포트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "포트 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/ports/{portId}")
    public ResponseEntity<Void> deletePort(@PathVariable String portId) {
        networkService.deletePort(portId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "포트 목록 조회", description = "포트 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포트 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/ports")
    public ResponseEntity<ResponseWrapper<List<PortDetailsDto>>> listPorts() {
        List<PortDetailsDto> ports = networkService.listPorts();
        return ResponseWrapper.success(ports);
    }

    @Operation(summary = "포트 상세 정보 조회", description = "특정 포트의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포트 상세 정보 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortDetailsDto.class))),
            @ApiResponse(responseCode = "404", description = "포트를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/ports/{portId}")
    public ResponseEntity<ResponseWrapper<PortDetailsDto>> getPortDetails(@PathVariable String portId) {
        PortDetailsDto port = networkService.getPortDetails(portId);
        if (port == null) {
            return ResponseWrapper.error("포트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        return ResponseWrapper.success(port);
    }


    // Router
    @Operation(summary = "라우터 생성", description = "새로운 라우터를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "라우터 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouterDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/routers")
    public ResponseEntity<ResponseWrapper<RouterDetailsDto>> createRouter(@RequestBody RouterCreateDto routerCreateDto) {
        RouterDetailsDto createdRouter = networkService.createRouter(routerCreateDto);
        return ResponseWrapper.created(createdRouter);
    }

    @Operation(summary = "라우터 업데이트", description = "기존 라우터를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라우터 업데이트 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouterDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/routers")
    public ResponseEntity<ResponseWrapper<RouterDetailsDto>> updateRouter(@RequestBody RouterUpdateDto routerUpdateDto) {
        RouterDetailsDto updatedRouter = networkService.updateRouter(routerUpdateDto);
        return ResponseWrapper.success(updatedRouter);
    }

    @Operation(summary = "라우터 삭제", description = "라우터를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "라우터 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/routers/{routerId}")
    public ResponseEntity<Void> deleteRouter(@PathVariable String routerId) {
        networkService.deleteRouter(routerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "라우터 목록 조회", description = "라우터 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라우터 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouterDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/routers")
    public ResponseEntity<ResponseWrapper<List<RouterDetailsDto>>> listRouters() {
        List<RouterDetailsDto> routers = networkService.listRouters();
        return ResponseWrapper.success(routers);
    }

    @Operation(summary = "라우터 상세 정보 조회", description = "특정 라우터의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라우터 상세 정보 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouterDetailsDto.class))),
            @ApiResponse(responseCode = "404", description = "라우터를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/routers/{routerId}")
    public ResponseEntity<ResponseWrapper<RouterDetailsDto>> getRouterDetails(@PathVariable String routerId) {
        RouterDetailsDto router = networkService.getRouterDetails(routerId);
        if (router == null) {
            return ResponseWrapper.error("라우터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        return ResponseWrapper.success(router);
    }

    @Operation(summary = "라우터 인터페이스 연결", description = "라우터와 인터페이스를 연결합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라우터 연결 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/routers/attach")
    public ResponseEntity<Void> attachRouter(@RequestBody RouterAttachDetachDto routerAttachDetachDto) {
        networkService.attachRouter(routerAttachDetachDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "라우터 인터페이스 연결 해제", description = "라우터와 인터페이스의 연결을 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라우터 연결 해제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/routers/detach")
    public ResponseEntity<Void> detachRouter(@RequestBody RouterAttachDetachDto routerAttachDetachDto) {
        networkService.detachRouter(routerAttachDetachDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "라우터 AdminState 변경", description = "라우터의 AdminState를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라우터 AdminState 변경 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/routers/state")
    public ResponseEntity<Void> toggleStateRouter(@RequestBody RouterToggleStateDto routerToggleStateDto) {
        networkService.toggleStateRouter(routerToggleStateDto);
        return ResponseEntity.noContent().build();
    }


    //Floating IP
    @Operation(summary = "Floating IP 생성", description = "새로운 Floating IP를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Floating IP 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FloatingIpDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/fips")
    public ResponseEntity<ResponseWrapper<FloatingIpDetailsDto>> createFloatingIp(FloatingIpCreateDto floatingIpCreateDto) {
        FloatingIpDetailsDto floatingIp = networkService.createFloatingIp(floatingIpCreateDto);
        return ResponseWrapper.success(floatingIp);
    }

    @Operation(summary = "Floating IP 목록 조회", description = "Floating IP 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Floating IP 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FloatingIpDetailsDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/fips")
    public ResponseEntity<ResponseWrapper<List<FloatingIpDetailsDto>>> listFloatingIps() {
        List<FloatingIpDetailsDto> floatingIps = networkService.listFloatingIps();
        return ResponseWrapper.success(floatingIps);
    }

    @Operation(summary = "Floating IP 삭제", description = "Floating IP를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Floating IP 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/fips/{floatingIpId}")
    public ResponseEntity<Void> deleteFloatingIp(@PathVariable String floatingIpId) {
        networkService.deleteFloatingIp(floatingIpId);
        return ResponseEntity.noContent().build();
    }
}
