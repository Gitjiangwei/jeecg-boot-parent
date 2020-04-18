package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Category;

import java.util.List;

public interface NewCategoryMapper extends BaseMapper<Category> {

    List<Category> qryList(Category category);

    List<Category> qryCategory(Category category);


    int saveCategory(Category category);


    int updateCategory(Category category);

    int deleteCategorys(@Param("delList") List<String> delList);

    int deleteCategory(@Param("id") String id);

    /**
     * 根据商品分类ID，查询商品类型名称
     * @param categoryIds
     * @return
     */
    List<Category> qryByIds(@Param("cateLists") List<String> categoryIds);

}
