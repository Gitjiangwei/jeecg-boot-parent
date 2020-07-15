package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.kunze.diansh.controller.vo.RiderVo;
import org.kunze.diansh.entity.Rider;
import org.kunze.diansh.mapper.RiderMapper;
import org.kunze.diansh.service.IRiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RiderServiceImpl extends ServiceImpl<RiderMapper, Rider> implements IRiderService {

    @Autowired
    private RiderMapper riderMapper;


    /**
     * 添加骑手信息
     *jw
     * @param rider
     * @return
     */
    @Override
    public Boolean saveRider(Rider rider) {
        Boolean isFlag = false;
        rider.setId(UUID.randomUUID().toString().replace("-",""));
        int result = riderMapper.saveRider(rider);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }

    /***
     * 修改骑手信息
     * @param rider
     * @return
     */
    @Override
    public Boolean editRider(Rider rider) {
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(rider.getId())){
            int result = riderMapper.editRider(rider);
            if(result>0){
                isFlag = true;
            }
        }

        return isFlag;
    }

    /***
     * 查询骑手信息
     * @param rider
     * @return
     */
    @Override
    public PageInfo<RiderVo> queryRiderList(Rider rider,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<RiderVo> riderVoList = riderMapper.queryRiderList(rider);
        PageInfo<RiderVo> pageInfo = new PageInfo<RiderVo>(riderVoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /***
     * 删除骑手信息
     * jw
     * @param ids
     * @return
     */
    @Override
    public Boolean delRider(String ids) {
        Boolean isFlag = false;
        if(ids != null && !ids.equals("")){
            char a = ids.charAt(ids.length()-1);
            if(a == ','){
                ids = ids.substring(0,ids.length()-1);
            }
            if(ids == null || ids.equals("")){
                return false;
            }
            List<String> list = new ArrayList<String>();
            if(ids.contains(",")){
                list = new ArrayList<String>(Arrays.asList(ids.split(",")));
            }else{
                list.add(ids);
            }
            int result = riderMapper.delRider(list);
            if(result>0){
                isFlag = true;
            }
        }

        return isFlag;
    }
}
