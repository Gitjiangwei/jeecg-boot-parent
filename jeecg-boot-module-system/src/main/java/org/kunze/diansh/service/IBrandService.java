package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.BrandVo;
import org.kunze.diansh.entity.Brand;
import org.kunze.diansh.entity.modelData.BrandModel;

import java.util.List;

public interface IBrandService extends IService<Brand> {

    /**
     * 查询商品品牌
     *
     * @param brandModel
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<BrandModel> qryBrand(BrandModel brandModel, Integer pageNo, Integer pageSize);

    /**
     * 添加商品品牌
     *
     * @param brand
     * @return
     */
    Boolean saveBrand(BrandVo brandVo);

    /**
     * 修改商品品牌
     *
     * @param brandVo
     * @return
     */
    Boolean updateBrand(BrandVo brandVo);

    /**
     * 删除商品品牌
     *
     * @param bids
     * @return
     */
    Boolean delBrands(String bids);

    /**
     * 查询品牌是否被引用，引用的品牌不能被删除
     *
     * @param brandId 品牌Id
     * @return true：可以删除，false：不可以删除
     */
    Boolean qryIsFlag(String brandId);

}
