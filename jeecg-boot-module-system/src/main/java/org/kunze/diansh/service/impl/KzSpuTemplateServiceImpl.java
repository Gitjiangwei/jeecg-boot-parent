package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.KzSpuTemplatelBo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.entity.modelData.KzSpuTemplateModel;
import org.kunze.diansh.mapper.KzSpuTemplateMapper;
import org.kunze.diansh.service.IKzSpuTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Transactional
    @Override
    public int addsTemplate(List<KzSpuTemplatelBo> kzSpuTemplatelBo) {

        try{
            return kzSpuTemplateMapper.addsTemplate(kzSpuTemplatelBo);
        }catch (Exception e )
        {
            log.error("插入失败");
            return 0;
        }


    }

}
