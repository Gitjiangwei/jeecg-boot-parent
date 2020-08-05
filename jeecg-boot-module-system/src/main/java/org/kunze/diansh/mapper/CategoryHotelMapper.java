package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.CategoryHotel;

public interface CategoryHotelMapper extends BaseMapper<CategoryHotel> {

    //添加分类 分类类型为餐饮分类
    Integer addCategoryHotel(CategoryHotel categoryHotel);

    //修改分类 分类类型为餐饮分类
    Integer updateCategoryHotel(@Param("hotel") CategoryHotel categoryHotel);

    //删除分类 通过id
    Integer delCategoryHotelById(@Param("id") String id);
}
