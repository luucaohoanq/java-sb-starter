/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.data;

import com.orchid.orchidbe.domain.account.Account;
import com.orchid.orchidbe.domain.category.Category;
import com.orchid.orchidbe.domain.orchid.Orchid;
import com.orchid.orchidbe.domain.order.Order;
import com.orchid.orchidbe.domain.order.OrderDetail;
import com.orchid.orchidbe.domain.role.Role;
import com.orchid.orchidbe.domain.role.Role.RoleName;
import com.orchid.orchidbe.repositories.AccountRepository;
import com.orchid.orchidbe.repositories.CategoryRepository;
import com.orchid.orchidbe.repositories.OrchidRepository;
import com.orchid.orchidbe.repositories.OrderDetailRepository;
import com.orchid.orchidbe.repositories.OrderRepository;
import com.orchid.orchidbe.repositories.RoleRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("h2")
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final AccountRepository accountRepository;
  private final CategoryRepository categoryRepository;
  private final OrchidRepository orchidRepository;
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final PasswordEncoder passwordEncoder;

  /*
  - Reset sequences to avoid conflicts with existing data

  SELECT setval('accounts_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM accounts), false);
  SELECT setval('categories_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM categories), false);
  SELECT setval('orchids_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM orchids), false);
  SELECT setval('order_details_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM order_details), false);
  SELECT setval('orders_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM orders), false);
  SELECT setval('roles_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM roles), false);
  SELECT setval('tokens_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM tokens), false);
  */

  @Override
  public void run(String... args) throws Exception {

    var hashedPassword = passwordEncoder.encode("Iloveyou123^^");

    // Create roles if empty
    List<Role> roles;
    if (roleRepository.findAll().isEmpty()) {
      var role1 = Role.builder().name(RoleName.ADMIN).build();
      var role2 = Role.builder().name(RoleName.MANAGER).build();
      var role3 = Role.builder().name(RoleName.USER).build();
      var role4 = Role.builder().name(RoleName.STAFF).build();
      roles = roleRepository.saveAll(Arrays.asList(role1, role2, role3, role4));
    } else {
      roles = roleRepository.findAll();
    }

    // Create categories if empty
    List<Category> categories;
    if (categoryRepository.findAll().isEmpty()) {
      var cat1 = Category.builder().name("Orchids").build();
      var cat2 = Category.builder().name("Exotic Flowers").build();
      var cat3 = Category.builder().name("Indoor Plants").build();
      categories = categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3));
    } else {
      categories = categoryRepository.findAll();
    }

    // Create orchids if empty
    List<Orchid> orchids;
    if (orchidRepository.findAll().isEmpty()) {
      var o1 =
          Orchid.builder()
              .isNatural(true)
              .description("Natural orchid")
              .name("Phalaenopsis")
              .url(
                  "https://images.unsplash.com/photo-1610397648930-477b8c7f0943?q=80&w=730&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
              .price(10.0)
              .category(categories.isEmpty() ? null : categories.get(0))
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build();

      var o2 =
          Orchid.builder()
              .isNatural(false)
              .description("Hybrid orchid")
              .name("Cattleya")
              .url(
                  "https://plus.unsplash.com/premium_photo-1673931249523-69dcbace086b?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
              .price(15.0)
              .category(categories.size() > 1 ? categories.get(1) : null)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build();

      var o3 =
          Orchid.builder()
              .isNatural(true)
              .description("Random orchid")
              .name("Dendrobium")
              .url(
                  "https://images.unsplash.com/photo-1562133558-4a3906179c67?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
              .price(20.0)
              .category(categories.size() > 2 ? categories.get(2) : null)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build();

      orchids = orchidRepository.saveAll(Arrays.asList(o1, o2, o3));
    } else {
      orchids = orchidRepository.findAll();
    }

    // Create accounts if empty
    List<Account> accounts;
    if (accountRepository.findAll().isEmpty()) {
      var acc1 =
          Account.builder()
              .name("Admin")
              .email("admin@gmail.com")
              .password(hashedPassword)
              .role(roles.isEmpty() ? null : roles.get(0))
              .build();

      var acc2 =
          Account.builder()
              .name("Manager")
              .email("manager@gmail.com")
              .password(hashedPassword)
              .role(roles.size() > 1 ? roles.get(1) : null)
              .build();

      var acc3 =
          Account.builder()
              .name("User")
              .email("user@gmail.com")
              .password(hashedPassword)
              .role(roles.size() > 2 ? roles.get(2) : null)
              .build();

      var acc4 =
          Account.builder()
              .name("Staff")
              .email("staff@gmail.com")
              .password(hashedPassword)
              .role(roles.size() > 2 ? roles.get(3) : null)
              .build();

      accounts = accountRepository.saveAll(Arrays.asList(acc1, acc2, acc3, acc4));
    } else {
      accounts = accountRepository.findAll();
    }

    // Create orders and order details if empty
    if (orderRepository.findAll().isEmpty()) {
      Order ord1 =
          Order.builder()
              .totalAmount(99.9)
              .orderDate(new Date())
              .orderStatus(Order.OrderStatus.PENDING)
              .account(accounts.isEmpty() ? null : accounts.get(2))
              .build();

      Order ord2 =
          Order.builder()
              .totalAmount(149.5)
              .orderDate(new Date())
              .orderStatus(Order.OrderStatus.PROCESSING)
              .account(accounts.size() > 1 ? accounts.get(2) : null)
              .build();

      Order ord3 =
          Order.builder()
              .totalAmount(199.0)
              .orderDate(new Date())
              .orderStatus(Order.OrderStatus.COMPLETED)
              .account(accounts.size() > 2 ? accounts.get(2) : null)
              .build();

      List<Order> savedOrders = orderRepository.saveAll(Arrays.asList(ord1, ord2, ord3));

      // OrderDetails
      OrderDetail d1 =
          OrderDetail.builder()
              .price(10.0)
              .quantity(2)
              .orchidId(orchids.isEmpty() ? null : orchids.get(0))
              .order(savedOrders.get(0))
              .build();

      OrderDetail d2 =
          OrderDetail.builder()
              .price(15.0)
              .quantity(1)
              .orchidId(orchids.size() > 1 ? orchids.get(1) : null)
              .order(savedOrders.get(1))
              .build();

      OrderDetail d3 =
          OrderDetail.builder()
              .price(20.0)
              .quantity(3)
              .orchidId(orchids.size() > 2 ? orchids.get(2) : null)
              .order(savedOrders.get(2))
              .build();

      orderDetailRepository.saveAll(Arrays.asList(d1, d2, d3));
    }
  }
}
