/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.orchid;

import com.orchid.orchidbe.apis.MyApiResponse;
import com.orchid.orchidbe.domain.orchid.OrchidDTO.OrchidRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/orchids")
@RequiredArgsConstructor
@Tag(name = "orchids", description = "Operation related to Orchid")
public class OrchidController {

  private final OrchidService orchidService;

  @GetMapping
  public ResponseEntity<?> getOrchids() {
    return ResponseEntity.ok(orchidService.getAll());
  }

  @GetMapping("/paged")
  @PreAuthorize("permitAll()")
  @Operation(
      summary = "List all blogs",
      description = "Retrieve a paginated list of blogs with optional pagination parameters")
  @Parameter(name = "page", description = "Page number (0-based)", example = "0")
  @Parameter(name = "size", description = "Number of records per page", example = "10")
  @Parameter(
      name = "sort",
      description =
          "Sorting criteria in the format: property,(asc|desc). "
              + "Default sort order is ascending. "
              + "Multiple sort criteria are supported.",
      example = "id,asc")
  public ResponseEntity<MyApiResponse<Page<OrchidRes>>> getAll(Pageable pageable) {
    var res = orchidService.getAll(pageable);
    return MyApiResponse.success(res);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrchidById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(orchidService.getById(id));
  }

  @PostMapping("")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
  public ResponseEntity<MyApiResponse<OrchidRes>> createOrchid(
      @Valid @RequestBody OrchidDTO.OrchidReq orchid) {
    return MyApiResponse.created(orchidService.add(orchid));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
  public ResponseEntity<MyApiResponse<Void>> updateOrchid(
      @PathVariable("id") Long id, @Valid @RequestBody OrchidDTO.OrchidReq orchid) {
    orchidService.update(id, orchid);
    return MyApiResponse.success();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
  public ResponseEntity<?> deleteOrchid(@PathVariable("id") Long id) {
    orchidService.deleteById(id);
    return MyApiResponse.success();
  }
}
