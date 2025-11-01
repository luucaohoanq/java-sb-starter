/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.orchid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orchid.orchidbe.base.BaseEntity;
import com.orchid.orchidbe.domain.category.Category;
import com.orchid.orchidbe.domain.orchid.OrchidDTO.OrchidRes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "orchids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Orchid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    private Long id;

    private boolean isNatural;

    private String description;

    @JsonProperty("orchidName")
    private String name;

    @JsonProperty("image")
    private String url;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public static OrchidRes from(Orchid orchid) {
        return new OrchidRes(
                orchid.getId(),
                orchid.isNatural(),
                orchid.getDescription(),
                orchid.getName(),
                orchid.getUrl(),
                orchid.getPrice(),
                orchid.getCategory().getId(),
                orchid.getCreatedAt(),
                orchid.getUpdatedAt());
    }

    public static Orchid toEntity(OrchidDTO.OrchidReq dto) {
        return Orchid.builder()
                .isNatural(dto.isNatural())
                .description(dto.description())
                .name(dto.name())
                .url(
                        dto.url() == null
                                ? "https://images.unsplash.com/photo-1610397648930-477b8c7f0943?q=80&w=730&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                                : dto.url()) // Ensure URL is not null
                .price(dto.price())
                .category(Category.builder().id(dto.categoryId()).build())
                .build();
    }
}
