/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.order;

import com.orchid.orchidbe.domain.order.OrderDTO.OrderRes;
import java.util.List;

public interface OrderService {

    List<OrderDTO.OrderRes> getAll();

    OrderRes getById(Long id);

    void add(OrderDTO.OrderReq order);

    void update(Long id, OrderDTO.OrderReq order);

    void delete(Long id);

    List<OrderDTO.OrderRes> getByUserId(Long userId);
}
