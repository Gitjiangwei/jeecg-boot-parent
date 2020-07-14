package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.controller.bo.KzSpuTemplatelBo;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.entity.modelData.KzSpuTemplateModel;
import org.kunze.diansh.mapper.KzSpuTemplateMapper;
import org.kunze.diansh.service.IKzSpuTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class KzSpuTemplateServiceImpl extends ServiceImpl<KzSpuTemplateMapper,KzSpuTemplate>  implements IKzSpuTemplateService{



    @Autowired
    private  KzSpuTemplateMapper kzSpuTemplateMapper;



    /**
     * 根据I分类ID查询对应图片
     * @param cid
     * @return
     */
    @Override
    public PageInfo<KzSpuTemplateModel> qryTemplateListById(String cid, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<KzSpuTemplateModel> KzSpuTemplateList = kzSpuTemplateMapper.qryTemplateListById(cid);
        PageInfo pageInfo = new PageInfo<KzSpuTemplateModel>(KzSpuTemplateList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }


    /**
     * 批量添加图片
     * @param kzSpuTemplatelBo
     * @return
     * **/
    @Override
    @Transactional
    public int addsTemplate(List<KzSpuTemplateModel> kzSpuTemplatelBo) {

        try{
            return kzSpuTemplateMapper.addsTemplate(kzSpuTemplatelBo);
        }catch (Exception e )
        {
            log.error("插入失败");
            return 0;
        }


    }


    /**
     * 删除商品
     * @param spuList
     * @return
     */
    @Override
    @Transactional
    public Boolean deleteSpu(List spuList) {
        boolean resultFlag = false;
        Integer delNum = kzSpuTemplateMapper.deleteSpu("",spuList);
        if(delNum>0){
           return true;
        }
        return resultFlag;
    }

}
