/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain;

import com.orchid.orchidbe.apis.MyApiResponse;
import com.orchid.orchidbe.domain.category.Category;
import com.orchid.orchidbe.domain.category.CategoryService;
import com.orchid.orchidbe.domain.orchid.OrchidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/public")
@RequiredArgsConstructor
@Deprecated
@Tag(name = "Public API", description = "Public endpoints that don't require authentication")
public class PublicController {

  // Account Controller

  // Role Controller

  // Orchid Controller
  private final OrchidService orchidService;

  @GetMapping("/orchids")
  public ResponseEntity<?> getOrchids() {
    return ResponseEntity.ok(orchidService.getAll());
  }

  @GetMapping("/orchids/{id}")
  public ResponseEntity<?> getOrchidById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(orchidService.getById(id));
  }

  // Order Controller

  // Category Controller
  private final CategoryService categoryService;

  @GetMapping("/categories")
  @Operation(summary = "Get all categories", description = "Returns a list of all categories")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories")
  public ResponseEntity<MyApiResponse<List<Category>>> getCategories() {
    return MyApiResponse.success(categoryService.getAll());
  }

  @GetMapping("/categories/{id}")
  @Operation(summary = "Get category by ID", description = "Returns a category by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
      })
  public ResponseEntity<MyApiResponse<Category>> getCategoryById(@PathVariable Long id) {
    return MyApiResponse.success(categoryService.getById(id));
  }

  // Token Controller

}
