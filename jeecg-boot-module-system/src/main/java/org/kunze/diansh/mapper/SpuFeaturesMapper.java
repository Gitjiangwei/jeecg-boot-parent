package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.SpuFeatures;

import java.util.List;

public interface SpuFeaturesMapper extends BaseMapper<SpuFeatures> {

    int saveSpuFeatures(@Param("list") List<SpuFeatures> spuFeatures);
}
