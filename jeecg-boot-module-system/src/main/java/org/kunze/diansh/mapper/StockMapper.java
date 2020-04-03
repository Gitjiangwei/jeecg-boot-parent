package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Stock;

import java.util.List;

public interface StockMapper extends BaseMapper<Stock> {


    int saveStock(@Param("stocks") List<Stock> stocks);
}
