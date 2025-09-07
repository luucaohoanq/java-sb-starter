package com.orchid.orchidbe.domain.order;

import com.orchid.orchidbe.domain.account.AccountService;
import com.orchid.orchidbe.domain.order.OrderDTO.OrderRes;
import com.orchid.orchidbe.repositories.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AccountService accountService;

    @Override
    public List<OrderDTO.OrderRes> getAll() {
        return orderRepository.findAll()
            .stream()
            .map(Order::fromEntity)
            .toList();
    }

    @Override
    public OrderDTO.OrderRes getById(Long id) {
        return orderRepository.findById(id)
            .map(Order::fromEntity)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    @Override
    public void add(OrderDTO.OrderReq order) {
        var account = accountService.getById(order.accountId());
        orderRepository.save(Order.toEntity(order, account));
    }

    @Override
    public void update(Long id, OrderDTO.OrderReq order) {
        var existingOrder = getById(id);
        if (!existingOrder.id().equals(order.id())) {
            throw new IllegalArgumentException("Cannot update order with different id");
        }
        var account = accountService.getById(order.accountId());
        orderRepository.save(Order.toEntity(order, account));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<OrderRes> getByUserId(Long userId) {
        return orderRepository.findByAccountId(userId)
            .stream()
            .map(Order::fromEntity)
            .toList();
    }
}
