package com.aoldacloud.console.domain.network;

import com.aoldacloud.console.domain.network.dto.PortCreateDto;
import com.aoldacloud.console.domain.network.dto.PortDetailsDto;
import com.aoldacloud.console.domain.network.dto.SubnetCreateDto;
import com.aoldacloud.console.domain.network.dto.SubnetDetailsDto;
import com.aoldacloud.console.global.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1.0/network")
@RequiredArgsConstructor
public class NetworkController {

    private final NetworkService networkService;

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
    public ResponseEntity<Void> deleteServer(@PathVariable String subnetId) {
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
    public ResponseEntity<ResponseWrapper<List<SubnetDetailsDto>>> listServers() {
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
    public ResponseEntity<ResponseWrapper<SubnetDetailsDto>> getServerDetails(@PathVariable String subnetId) {
        SubnetDetailsDto subnet = networkService.getSubnetDetails(subnetId);
        if (subnet == null) {
            return ResponseWrapper.error("서브넷을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        return ResponseWrapper.success(subnet);
    }

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
}
