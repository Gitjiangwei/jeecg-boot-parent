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

    /***
     * 后台查询轮播图信息
     * @param wheel
     * @return
     */
    List<Wheel> qeryWheelbackstage(Wheel wheel);


    /**
     * 根据轮播图id查询超市id
     * @param id
     * @return
     */
    List<String> selectByShopId(@Param("id") String id);


    /**
     * 根据轮播图id查询轮播图
     * @param id
     * @return
     */
    String queryImages(@Param("id") String id);

}
