/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.exceptions;

public class JwtAuthenticationException extends RuntimeException {

  public JwtAuthenticationException(String message) {
    super(message);
  }

  public JwtAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
