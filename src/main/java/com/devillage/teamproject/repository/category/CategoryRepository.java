package com.devillage.teamproject.repository.category;

import com.devillage.teamproject.entity.Category;
import com.devillage.teamproject.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoriesByCategoryType(CategoryType categoryType);
}
