package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.RiderVo;
import org.kunze.diansh.entity.Rider;

import java.util.List;

public interface RiderMapper extends BaseMapper<Rider> {

    /***
     * 添加骑手信息
     * @param rider
     * @return
     */
    int saveRider(Rider rider);

    /**
     * 修改骑手信息
     * @param rider
     * @return
     */
    int editRider(Rider rider);


    /***
     * 查询骑手信息
     * jw
     * @param rider
     * @return
     */
    List<RiderVo> queryRiderList(Rider rider);


    /**
     * 删除骑手信息
     * @param stringList
     * jw
     * @return
     */
    int delRider(@Param("list") List<String> stringList);

}
