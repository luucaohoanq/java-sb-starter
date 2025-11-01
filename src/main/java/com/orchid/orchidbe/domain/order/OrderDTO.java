/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.order;

import com.orchid.orchidbe.domain.order.Order.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public interface OrderDTO {

    record OrderReq(
            Long id,
            @Min(value = 0, message = "Total amount must be greater than or equal to 0")
                    Double totalAmount,
            Date orderDate,
            OrderStatus orderStatus,
            @NotNull(message = "Account ID cannot be null") Long accountId) {}

    record OrderRes(
            Long id, Double totalAmount, Date orderDate, OrderStatus orderStatus, Long accountId) {}
}
