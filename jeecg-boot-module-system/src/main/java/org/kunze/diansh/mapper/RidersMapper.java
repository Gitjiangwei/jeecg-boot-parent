package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Riders;


@Mapper
public interface RidersMapper extends BaseMapper<Riders> {


    Riders queryRiderInfo(@Param(value = "rider") Riders rider);


}
