package com.orchid.orchidbe.domain.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.order.OrderDTO.OrderRes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    private Long id;

    private Double totalAmount;

    private Date orderDate;

    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        CANCELLED,
    }

    public static OrderRes fromEntity(Order order) {
        return new OrderRes(
            order.getId(),
            order.getTotalAmount(),
            order.getOrderDate(),
            order.getOrderStatus(),
            order.getAccount().getId()
        );
    }

    public static Order toEntity(OrderDTO.OrderReq orderReq, Account account) {
        return Order.builder()
            .totalAmount(orderReq.totalAmount())
            .orderDate(orderReq.orderDate())
            .orderStatus(orderReq.orderStatus())
            .account(account)
            .build();
    }

}

