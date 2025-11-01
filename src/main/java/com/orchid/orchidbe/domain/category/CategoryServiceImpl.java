/**
 * Copyright (c) 2025 lcaohoanq. All rights reserved.
 *
 * This software is the confidential and proprietary information of lcaohoanq.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with lcaohoanq.
 */
package com.orchid.orchidbe.domain.category;

import com.orchid.orchidbe.repositories.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Override
    public void save(CategoryDTO.CategoryReq category) {

        if (categoryRepository.existsByName(category.name())) {
            throw new IllegalArgumentException(
                    "Category with name " + category.name() + " already exists");
        }

        var newCategory = new Category();
        newCategory.setName(category.name());

        categoryRepository.save(newCategory);
    }

    @Override
    public void update(Long id, CategoryDTO.CategoryReq category) {

        var existingCategory = getById(id);

        if (categoryRepository.existsByNameAndIdNot(category.name(), id)) {
            throw new IllegalArgumentException(
                    "Category with name " + category.name() + " already exists");
        }

        existingCategory.setName(category.name());

        categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        var existingCategory = getById(id);
        categoryRepository.delete(existingCategory);
    }
}
