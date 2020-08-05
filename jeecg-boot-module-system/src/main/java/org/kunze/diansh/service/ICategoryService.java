package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysCategory;
import org.kunze.diansh.entity.Category;
import org.kunze.diansh.entity.CategoryHotel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ICategoryService extends IService<Category> {

    /**
     * 查询分类接口
     *
     * @param pid
     * @return
     */
    List<Category> qryList(String pid, String id);

    /**
     * 添加商品分类接口
     *
     * @param category
     * @return
     */
    Boolean saveCategory(Category category);

    /**
     * 商品分类修改接口
     *
     * @param category
     * @return
     */
    Boolean updateCategory(Category category);

    /**
     * 商品分类批量删除
     *
     * @param ids
     * @return
     */
    Boolean deleteCategorys(String ids);

    /**
     * 商品分类单个删除
     *
     * @param id
     * @return
     */
    Boolean deleteCategory(String id);


    /**
     * 根据分类名称 模糊查询相关分类
     * @param name 分类名称
     * @return 查询到的数据
     */
    List<Category> qryCategoryByName(String name);

    /**
     * 获取分类的全部数据
     * @return
     */
    Collection<Category> getAllCategory(String cateId);

    /**
     * 查询全部一级分类
     * @param name
     * @return
     */
    PageInfo<Map<String,String>> queryCid1(String name, Integer pageNo, Integer pageSize);

    /**
     * 获取分类 通过店铺id 类型为饭店分类菜单
     * @param shopId 店铺id
     * @return
     */
    Collection<CategoryHotel> getHotelCategoryByShopId(String shopId,String isShow);

    /**
     * 添加分类 分类类型为餐饮分类
     * @param categoryHotel
     * @return
     */
    Boolean addCategoryHotel(CategoryHotel categoryHotel);

    /**
     * 修改分类 分类类型为餐饮分类
     * @param categoryHotel
     * @return
     */
    Boolean updateCategoryHotel(CategoryHotel categoryHotel);

    /**
     * 删除分类 通过id
     * @param id
     * @return
     */
    Boolean delCategoryHotelById(String id);
}
