package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysCategory;
import org.kunze.diansh.entity.Category;

import java.util.Collection;
import java.util.List;

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
    Collection<Category> getAllCategory();
}
