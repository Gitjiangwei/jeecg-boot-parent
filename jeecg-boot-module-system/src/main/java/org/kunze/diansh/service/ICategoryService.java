package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysCategory;
import org.kunze.diansh.entity.Category;

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


}
