package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Specification;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface ISpecificationService extends IService<Specification> {

    /**
     * 查询商品规格参数模板，json格式。
     * @param categoryId
     * @return
     */
    Specification qrySpecification(String categoryId);


    /**
     * 添加商品规格参数模板，specificaions参数为json格式
     *@param categoryId 分类id
     * @param specifications 参数为json格式
     * @return ok:添加成功；error：添加失败；NOT：参数为空
     */
    String saveSpecification(String categoryId,String specifications);


    /**
     * 修改商品规格参数模板，specificaions参数为json格式
     * @param categoryId 分类id
     * @param specifications 参数为json格式
     * @return 添加成功；error：添加失败；NOT：参数为空
     */
    String updateSpecification(String categoryId,String specifications);


    /**
     * 删除商品规格参数模板
     * @param categoryIds
     * @return
     */
    Boolean delSpecification(String categoryIds);
}
