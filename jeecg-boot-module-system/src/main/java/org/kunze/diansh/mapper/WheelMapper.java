package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Wheel;

import java.util.List;

public interface WheelMapper extends BaseMapper<Wheel> {

    int saveWheel(Wheel wheel);

    List<Wheel> qeryWheel(Wheel wheel);

    int updateWheel(Wheel wheel);

    int delWheels(@Param("list") List<String> delWheels);

    int updateIsFlag(@Param("isFlag")String isFlag,
                     @Param("updateName") String updateName,
                     @Param("list") List<String> isFlagWheels);

}
