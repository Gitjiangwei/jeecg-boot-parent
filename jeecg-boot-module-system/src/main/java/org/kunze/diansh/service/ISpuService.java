package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.controller.vo.SpuVo;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.modelData.SpuModel;

import java.util.List;

public interface ISpuService extends IService<Spu> {

    /**
     * 商品查询
     * @param spuVo
     * @return
     */
    PageInfo<SpuModel> qrySpuList(SpuVo spuVo,Integer pageNo,Integer pageSize);


    /**
     * 查询品牌以作下拉框使用
     * @return
     */
    List<SpuBrandVo> qrySpuBrand();

    /**
     * 根据商品分类查询品牌
     * @param categoryId
     * @return
     */
    List<SpuBrandVo> qrySpuBrands(String categoryId);

    /**
     * 添加商品
     * @param spuBo
     * @return
     */
    Boolean saveSpu(SpuBo spuBo);
}
