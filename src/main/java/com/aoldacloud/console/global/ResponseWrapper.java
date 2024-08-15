package com.aoldacloud.console.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "API 응답을 감싸는 공통 래퍼 클래스")
public class ResponseWrapper<T> {

  @Schema(description = "응답 성공 여부", example = "true")
  private final boolean success;

  @Schema(description = "응답 데이터")
  private final T data;

  @Schema(description = "오류 메시지", example = "에러가 발생했습니다.")
  private final String error;


  @Schema(description = "결과 시간", example = "2024-08-15T04:38:41.636836")
  private final String timeStamp = LocalDateTime.now().toString();

  @Schema(description = "성공적인 응답을 생성합니다.")
  public static <T> ResponseEntity<ResponseWrapper<T>> success(T data) {
    return ResponseEntity.ok(new ResponseWrapper<>(true, data, null));
  }

  @Schema(description = "성공적인 생성 응답을 생성합니다.")
  public static <T> ResponseEntity<ResponseWrapper<T>> created(T data) {
    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(true, data, null));
  }

  @Schema(description = "오류 응답을 생성합니다.")
  public static <T> ResponseEntity<ResponseWrapper<T>> error(String error, HttpStatus status) {
    return ResponseEntity.status(status).body(new ResponseWrapper<>(false, null, error));
  }
}
