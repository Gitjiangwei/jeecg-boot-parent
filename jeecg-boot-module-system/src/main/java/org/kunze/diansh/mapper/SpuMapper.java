package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.modelData.SpuModel;

import java.util.List;

public interface SpuMapper extends BaseMapper<Spu> {

    List<SpuModel> qrySpuList(Spu spu);

    List<SpuBrandVo> qrySpuBrand();

    int saveSpu(Spu spu);

    List<SpuBrandVo> qrySpuBrands(@Param("categoryId") String categoryId);
}
