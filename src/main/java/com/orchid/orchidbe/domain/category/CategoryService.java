/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.category;

import java.util.List;

public interface CategoryService {

  List<Category> getAll();

  Category getById(Long id);

  void save(CategoryDTO.CategoryReq category);

  void update(Long id, CategoryDTO.CategoryReq category);

  void delete(Long id);
}
