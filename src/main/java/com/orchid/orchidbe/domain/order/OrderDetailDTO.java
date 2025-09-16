/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.order;

public interface OrderDetailDTO {

  record OrderDetailReq(Integer orderId, Integer orchidId, Integer quantity, Double price) {}

  record OrderDetailRes(
      Integer id, Integer orderId, Integer orchidId, Integer quantity, Double price) {}
}
