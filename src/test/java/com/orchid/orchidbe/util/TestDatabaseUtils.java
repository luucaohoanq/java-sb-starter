/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.util;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class for test database operations. Provides methods to safely clean up test databases
 * with foreign key constraints.
 */
@Component
@RequiredArgsConstructor
public class TestDatabaseUtils {

  private final EntityManager entityManager;

  /**
   * Cleans up all test data by disabling foreign key checks, truncating tables, and re-enabling
   * foreign key checks. This is safe for H2 test databases.
   */
  @Transactional
  public void cleanDatabase() {
    // Disable foreign key checks
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

    // Clean all tables in proper order
    entityManager.createNativeQuery("DELETE FROM order_details").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM orders").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM tokens").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM accounts").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM roles").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM categories").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM orchids").executeUpdate();

    // Reset sequences
    entityManager
        .createNativeQuery("ALTER SEQUENCE IF EXISTS accounts_seq RESTART WITH 1")
        .executeUpdate();
    entityManager
        .createNativeQuery("ALTER SEQUENCE IF EXISTS roles_seq RESTART WITH 1")
        .executeUpdate();
    entityManager
        .createNativeQuery("ALTER SEQUENCE IF EXISTS orders_seq RESTART WITH 1")
        .executeUpdate();
    entityManager
        .createNativeQuery("ALTER SEQUENCE IF EXISTS tokens_seq RESTART WITH 1")
        .executeUpdate();
    entityManager
        .createNativeQuery("ALTER SEQUENCE IF EXISTS categories_seq RESTART WITH 1")
        .executeUpdate();
    entityManager
        .createNativeQuery("ALTER SEQUENCE IF EXISTS orchids_seq RESTART WITH 1")
        .executeUpdate();

    // Re-enable foreign key checks
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

    // Flush and clear persistence context
    entityManager.flush();
    entityManager.clear();
  }
}
