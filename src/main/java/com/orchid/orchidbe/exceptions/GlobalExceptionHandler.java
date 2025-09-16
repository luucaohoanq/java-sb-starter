/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.exceptions;

import com.orchid.orchidbe.apis.ApiResponse;
import com.orchid.orchidbe.apis.MyApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<MyApiResponse<Object>> handleNullPointerException(NullPointerException e) {
    log.error("NullPointerException: ", e);
    return MyApiResponse.error(
        HttpStatus.BAD_REQUEST, "Null pointer exception occurred", e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<MyApiResponse<Object>> handleIllegalArgumentException(
      IllegalArgumentException e) {
    log.error("IllegalArgumentException: ", e);
    return MyApiResponse.error(HttpStatus.BAD_REQUEST, "Invalid argument provided", e.getMessage());
  }

  // Handle validation errors from @Valid annotation
  /*
  *   {
        "statusCode": 400,
        "message": "Validation failed",
        "fieldErrors": {
          "price": "Price must be greater than or equal to 0",
          "name": "Name is not blank"
        },
        "path": "/api/orchids",
        "timestamp": 1757986143.4317818
      }
  *
  * */
  //    @ExceptionHandler(MethodArgumentNotValidException.class)
  //    public ResponseEntity<MyApiResponse<Object>> handleValidationExceptions(
  //        MethodArgumentNotValidException ex,
  //        WebRequest request) {
  //        log.error("Validation error: {}", ex.getMessage());
  //
  //        Map<String, String> errors = new HashMap<>();
  //        ex.getBindingResult().getFieldErrors().forEach(error ->
  //                                                           errors.put(error.getField(),
  // error.getDefaultMessage()));
  //
  //        return MyApiResponse.validationError(errors);
  //    }

  /*
  *   {
        "status": "400 BAD_REQUEST",
        "message": "name: Name is not blank; price: Price must be greater than or equal to 0",
        "data": null,
        "errorCode": "VALIDATION_ERROR",
        "timestamp": "2025-09-16T08:33:31.842Z"
      }
  * */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    List<String> errorList =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
    String errors = String.join("; ", errorList);

    ApiResponse<Object> response =
        new ApiResponse<>(HttpStatus.BAD_REQUEST.toString(), errors, null, "VALIDATION_ERROR");
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // Handle validation errors from BindException (happens with @ModelAttribute validation)
  @ExceptionHandler(BindException.class)
  public ResponseEntity<MyApiResponse<Object>> handleBindExceptions(
      BindException ex, WebRequest request) {
    log.error("Binding error: {}", ex.getMessage());

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return MyApiResponse.validationError(errors);
  }

  // Handle authentication errors
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<MyApiResponse<Object>> handleBadCredentialsException(
      BadCredentialsException ex) {
    log.error("Authentication error: {}", ex.getMessage());
    return MyApiResponse.error(
        HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid username or password");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<MyApiResponse<Object>> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    log.error("JSON parse error: {}", ex.getMessage());
    return MyApiResponse.badRequest("Invalid JSON format in request body");
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<MyApiResponse<Object>> handleResponseStatusException(
      ResponseStatusException ex) {
    log.error("ResponseStatusException: {}", ex.getReason());
    return MyApiResponse.error((HttpStatus) ex.getStatusCode(), ex.getReason(), ex.getMessage());
  }
}
