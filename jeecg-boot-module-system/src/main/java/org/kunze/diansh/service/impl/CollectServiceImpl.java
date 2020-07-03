package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.CommonUtil;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Collect;
import org.kunze.diansh.mapper.CollectMapper;
import org.kunze.diansh.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {

    @Autowired
    private CollectMapper collectMapper;

    /**
     * 添加商品收藏记录
     * @param collect
     * @return
     */
    @Override
    public String insertCollect(Collect collect) {
        if(EmptyUtils.isEmpty(collect.getId())){
            //主键uuid
            collect.setId(UUID.randomUUID().toString().replace("-",""));
        }
        return collectMapper.insertCollect(collect)>0?collect.getId():"";
    }

    /**
     * 删除商品收藏记录
     * @param cList 收藏商品的主键集合
     * @return
     */
    @Override
    public Boolean deleteCollect(List<String> cList) {
        return collectMapper.deleteCollect(cList)>0?true:false;
    }

    /**
     * 查询当前用户的收藏商品
     * @param userId
     * @param shopId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectCollectByUId(String userId,String shopId) {
        return CommonUtil.toCamel(collectMapper.selectCollectByUId(userId,shopId));
    }
    /**
     * 查询当前用户收藏商品总数
     * @param userId
     * @return
     */
    @Override
    public Integer countCollectByUId(String userId,String shopId) {
        return collectMapper.countCollectByUId(userId,shopId);
    }

    /**
     * 查询当前商品是否被收藏
     * @param spuId
     * @param userId
     * @return
     */
    @Override
    public Collect isCollect(String userId,String spuId,String shopId){
        return collectMapper.isCollect(userId,spuId,shopId);
    }
}
