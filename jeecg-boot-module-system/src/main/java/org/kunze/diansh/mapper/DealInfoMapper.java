package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.kunze.diansh.controller.bo.DealInfoBo;
import org.kunze.diansh.controller.vo.DealInfoVo;
import org.kunze.diansh.entity.DealInfo;
import org.kunze.diansh.entity.modelData.DealInfoModel;

import java.util.List;

public interface DealInfoMapper extends BaseMapper<DealInfo> {


    /**
     * 查询历史交易额
     * @param dealInfoBo
     * @return
     */
    List<DealInfoModel> queryDealInfoList(DealInfoBo dealInfoBo);
}
