package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.SpuDetail;

import java.util.List;

public interface SpuDetailMapper extends BaseMapper<SpuDetail> {

    int saveSpuDetail(SpuDetail spuDetail);

    int updateSpuDetail(SpuDetail spuDetail);

    /**
     * 根据SpuId查询详情
     * @param spuId
     * @return
     */
    SpuDetail qreySpuDetail(@Param("spuId") String spuId);
}
