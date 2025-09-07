package com.orchid.orchidbe.domain.category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();
    Category getById(Long id);
    void save(CategoryDTO.CategoryReq category);
    void update(Long id, CategoryDTO.CategoryReq category);
    void delete(Long id);


}
