package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Brand;
import org.kunze.diansh.entity.modelData.BrandModel;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {

    List<BrandModel> qryBrand(BrandModel brandModel);

    int saveBrand(Brand brand);

    int updateBrand(Brand brand);

    int delBrands(@Param("list") List<String> delList);

    int qryIsFlag(@Param("brandIds") List<String> brandIds);

    List<Brand> qryByKeys(@Param("brandIds") List<String> brandIds);
}
