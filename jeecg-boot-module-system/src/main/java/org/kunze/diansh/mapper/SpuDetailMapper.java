package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.kunze.diansh.entity.SpuDetail;

public interface SpuDetailMapper extends BaseMapper<SpuDetail> {

    int saveSpuDetail(SpuDetail spuDetail);
}
