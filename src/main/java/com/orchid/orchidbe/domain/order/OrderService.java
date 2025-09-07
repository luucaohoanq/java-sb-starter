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
