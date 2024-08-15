package com.aoldacloud.console.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * IllegalArgumentException 발생 시 처리하는 메서드입니다.
   *
   * @param ex IllegalArgumentException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseWrapper<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseWrapper.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * MethodArgumentNotValidException 발생 시 처리하는 메서드입니다.
   *
   * @param ex MethodArgumentNotValidException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseWrapper<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return ResponseWrapper.error("Validation failed: " + ex.getBindingResult().toString(), HttpStatus.BAD_REQUEST);
  }


  /**
   * MethodArgumentTypeMismatchException 발생 시 처리하는 메서드입니다.
   *
   * @param ex MethodArgumentTypeMismatchException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ResponseWrapper<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    return ResponseWrapper.error("Argument type mismatch: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * MissingServletRequestParameterException 발생 시 처리하는 메서드입니다.
   *
   * @param ex MissingServletRequestParameterException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ResponseWrapper<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
    return ResponseWrapper.error("Missing request parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
  }

  /**
   * NoHandlerFoundException 발생 시 처리하는 메서드입니다.
   *
   * @param ex NoHandlerFoundException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ResponseWrapper<String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    return ResponseWrapper.error("No handler found: " + ex.getRequestURL(), HttpStatus.NOT_FOUND);
  }

  /**
   * HttpRequestMethodNotSupportedException 발생 시 처리하는 메서드입니다.
   *
   * @param ex HttpRequestMethodNotSupportedException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ResponseWrapper<String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
    return ResponseWrapper.error("Request method not supported: " + ex.getMethod(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * HttpMediaTypeNotSupportedException 발생 시 처리하는 메서드입니다.
   *
   * @param ex HttpMediaTypeNotSupportedException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ResponseWrapper<String>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
    return ResponseWrapper.error("Media type not supported: " + ex.getContentType(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * HttpMediaTypeNotAcceptableException 발생 시 처리하는 메서드입니다.
   *
   * @param ex HttpMediaTypeNotAcceptableException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<ResponseWrapper<String>> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
    return ResponseWrapper.error("Media type not acceptable", HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * AccessDeniedException 발생 시 처리하는 메서드입니다.
   *
   * @param ex AccessDeniedException 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseWrapper<String>> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseWrapper.error("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  /**
   * 그 외 발생할 수 있는 예외를 처리하는 메서드입니다.
   *
   * @param ex Exception 인스턴스
   * @return ResponseEntity<ResponseWrapper<String>> 에러 메시지와 상태 코드를 담은 응답
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseWrapper<String>> handleGeneralException(Exception ex) {
    return ResponseWrapper.error("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
