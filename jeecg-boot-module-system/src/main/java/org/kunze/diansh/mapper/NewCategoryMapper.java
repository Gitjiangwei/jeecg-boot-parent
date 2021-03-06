package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Category;
import org.kunze.diansh.entity.CategoryHotel;

import java.util.List;
import java.util.Map;

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


    /**
     * 根据分类名称 模糊查询相关分类
     * @param name
     * @return
     */
    List<Category> qryCategoryByName(@Param("name")String name);

    /**
     * 查询全部一级分类
     * @param name
     * @return
     */
    List<Map<String,String>> queryCid1(@Param("name") String name);

    /**
     * 获取分类 通过店铺id 类型为饭店分类菜单
     * @return
     */
    List<CategoryHotel> getHotelCategoryByShopId(@Param("shopId") String shopId);
}
