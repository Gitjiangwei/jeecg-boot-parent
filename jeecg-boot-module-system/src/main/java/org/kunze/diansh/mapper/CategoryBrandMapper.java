package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.CategoryBrand;

import java.util.List;

public interface CategoryBrandMapper extends BaseMapper<CategoryBrandMapper> {


    int saveCategoryBrand(@Param("list") List<CategoryBrand> categoryBrandList);

    int updateCategoryBrand(@Param("brandId") String brandId);

    int delCategoryBrand(@Param("list") List<String> delList);
}
