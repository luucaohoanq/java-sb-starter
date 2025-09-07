package com.orchid.orchidbe.domain.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface CategoryDTO {

    record CategoryReq(
        @NotBlank(message = "Category Name is not blank")
        @Size(min = 3, max = 50, message = "Category Name must be between 3 and 50 characters")
        String name
    ){

    }

}
