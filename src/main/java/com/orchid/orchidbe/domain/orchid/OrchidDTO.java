/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.orchid;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface OrchidDTO {

    record OrchidReq(
            boolean isNatural,
            String description,
            @NotBlank(message = "Name is not blank") String name,
            String url,
            @Min(value = 0, message = "Price must be greater than or equal to 0")
                    @Max(
                            value = 1000000000,
                            message = "Price must be less than or equal to 1,000,000,000")
                    Double price,
            @NotNull(message = "Category ID cannot be null") Long categoryId) {}

    record OrchidRes(
            Long id,
            boolean isNatural,
            String description,
            String name,
            String url,
            Double price,
            Long categoryId,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
                    LocalDateTime createdAt,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
                    LocalDateTime updatedAt) {}
}
