package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Shop;

public interface ShopMapper extends BaseMapper<Shop> {

    /**
     * 根据超市id查询超市信息
     * @param id
     * @return
     */
    Shop selectByKey(@Param("shopId") String id);
}
