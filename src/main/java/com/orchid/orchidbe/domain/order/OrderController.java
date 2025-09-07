package com.orchid.orchidbe.domain.order;

import com.orchid.orchidbe.apis.MyApiResponse;
import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.order.OrderDTO.OrderRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
@Tag(name = "orders", description = "Operation related to Order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<MyApiResponse<List<OrderDTO.OrderRes>>> getOrders() {
        return MyApiResponse.success(orderService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<MyApiResponse<OrderRes>> getOrderById(@PathVariable Long id) {
        return MyApiResponse.success(orderService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<MyApiResponse<Void>> createOrder(
        @RequestBody @Valid OrderDTO.OrderReq orderReq
    ) {
        orderService.add(orderReq);
        return MyApiResponse.created();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<MyApiResponse<Void>> updateOrder(
        @PathVariable Long id, @RequestBody OrderDTO.OrderReq orderReq
    ) {
        orderService.update(id, orderReq);
        return MyApiResponse.updated();
    }

    @GetMapping("/me/orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<MyApiResponse<List<OrderDTO.OrderRes>>> getMyOrders(
        Authentication authentication) {
        Account account = (Account) authentication.getPrincipal(); // principal là chính user từ token
        Long userId = account.getId();

        return MyApiResponse.success(orderService.getByUserId(userId));
    }


}
