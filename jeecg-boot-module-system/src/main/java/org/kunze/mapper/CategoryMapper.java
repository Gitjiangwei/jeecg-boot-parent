package org.kunze.mapper;

import org.kunze.entity.Category;

import java.util.List;

public interface CategoryMapper {

    List<Category> qryList(Category category);
}
