package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.KzSpuTemplatelBo;
import org.kunze.diansh.entity.KzSpuTemplate;
import org.kunze.diansh.entity.modelData.KzSpuTemplateModel;


import java.util.List;

public interface IKzSpuTemplateService extends IService<KzSpuTemplate> {


    /**
     * 根据分类ID查询对应图片
     * @param cid
     * @return
     */
    PageInfo<KzSpuTemplateModel> qryTemplateListById(String cid, Integer pageNo, Integer pageSize);

    /**
     * 批量添加图片
     * @param kzSpuTemplatelBo
     *
     * **/

    int addsTemplate(List<KzSpuTemplateModel> kzSpuTemplatelBo);
}
