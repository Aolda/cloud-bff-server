package com.aoldacloud.console.domain.compute;

import com.aoldacloud.console.domain.compute.dto.ServerCreateDto;
import com.aoldacloud.console.domain.compute.dto.ServerDetailsDto;
import com.aoldacloud.console.domain.compute.dto.ServerUpdateDto;
import com.aoldacloud.console.global.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/compute")
@RequiredArgsConstructor
@Tag(name = "Compute", description = "AoldaCloud 서버, 블록 스토리지 등과 관련된 API")
public class ComputeController {

  private final ComputeService computeService;

  @Operation(summary = "가상머신 생성", description = "새로운 가상머신을 생성합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "가상머신 생성 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerDetailsDto.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/servers")
  public ResponseEntity<ResponseWrapper<ServerDetailsDto>> createServer(@RequestBody ServerCreateDto serverCreateDto) {
    ServerDetailsDto createdServer = computeService.createServer(serverCreateDto);
    return ResponseWrapper.created(createdServer);
  }

  @Operation(summary = "가상머신 업데이트", description = "기존 가상머신을 업데이트합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "가상머신 업데이트 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerDetailsDto.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/servers")
  public ResponseEntity<ResponseWrapper<ServerDetailsDto>> updateServer(@RequestBody ServerUpdateDto serverUpdateDto) {
    ServerDetailsDto updatedServer = computeService.updateServer(serverUpdateDto);
    return ResponseWrapper.success(updatedServer);
  }

  @Operation(summary = "가상머신 삭제", description = "가상머신을 삭제합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "가상머신 삭제 성공"),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @DeleteMapping("/servers/{serverId}")
  public ResponseEntity<Void> deleteServer(@PathVariable String serverId) {
    computeService.deleteServer(serverId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "가상머신 목록 조회", description = "가상머신 목록을 조회합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "가상머신 목록 조회 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerDetailsDto.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/servers")
  public ResponseEntity<ResponseWrapper<List<ServerDetailsDto>>> listServers() {
    List<ServerDetailsDto> servers = computeService.listServers();
    return ResponseWrapper.success(servers);
  }

  @Operation(summary = "가상머신 상세 정보 조회", description = "특정 가상머신의 상세 정보를 조회합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "가상머신 상세 정보 조회 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerDetailsDto.class))),
          @ApiResponse(responseCode = "404", description = "가상머신을 찾을 수 없음",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/servers/{serverId}")
  public ResponseEntity<ResponseWrapper<ServerDetailsDto>> getServerDetails(@PathVariable String serverId) {
    ServerDetailsDto server = computeService.getServerDetails(serverId);
    if (server == null) {
      return ResponseWrapper.error("가상머신을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
    return ResponseWrapper.success(server);
  }

  @Operation(summary = "서버 액션 실행", description = "특정 서버에 대해 액션을 실행합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "서버 액션 성공",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/servers/{serverId}/action")
  public ResponseEntity<Void> performServerAction(@PathVariable String serverId, @RequestParam String action) {
    computeService.performServerAction(serverId, action);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "스냅샷 생성", description = "특정 가상머신의 스냅샷을 생성합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "스냅샷 생성 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/servers/{serverId}/snapshot")
  public ResponseEntity<ResponseWrapper<String>> createSnapshot(@PathVariable String serverId, @RequestParam String snapshotName) {
    String snapshotId = computeService.createSnapshot(serverId, snapshotName);
    return ResponseWrapper.success(snapshotId);
  }

  @Operation(summary = "서버 메타데이터 조회", description = "특정 가상머신의 메타데이터를 조회합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "서버 메타데이터 조회 성공",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/servers/{serverId}/metadata")
  public ResponseEntity<ResponseWrapper<Map<String, String>>> getServerMetadata(@PathVariable String serverId) {
    Map<String, String> metadata = computeService.getServerMetadata(serverId);
    return ResponseWrapper.success(metadata);
  }

  @Operation(summary = "서버 메타데이터 업데이트", description = "특정 가상머신의 메타데이터를 업데이트합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "서버 메타데이터 업데이트 성공",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/servers/{serverId}/metadata")
  public ResponseEntity<Void> updateServerMetadata(@PathVariable String serverId, @RequestBody Map<String, String> metadata) {
    computeService.updateServerMetadata(serverId, metadata);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "서버 메타데이터 항목 삭제", description = "특정 가상머신의 메타데이터 항목을 삭제합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "서버 메타데이터 항목 삭제 성공",
                  content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json"))
  })
  @DeleteMapping("/servers/{serverId}/metadata/{key}")
  public ResponseEntity<Void> deleteServerMetadataItem(@PathVariable String serverId, @PathVariable String key) {
    computeService.deleteServerMetadataItem(serverId, key);
    return ResponseEntity.ok().build();
  }
}
