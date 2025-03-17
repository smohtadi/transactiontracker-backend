package com.smohtadi.expenses.repositories;

import com.smohtadi.expenses.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
