package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.kunze.diansh.controller.bo.KzSpuTemplatelBo;
import org.kunze.diansh.entity.KzSpuTemplate;
import org.kunze.diansh.entity.modelData.KzSpuTemplateModel;
import java.util.List;


public interface KzSpuTemplateMapper extends BaseMapper<KzSpuTemplate> {

    /**
     * 根据商品ID查询对应图片
     * @param cid
     * @return
     */

    List<KzSpuTemplateModel> qryTemplateListById(String cid);

    /**
     *
     * 批量上传图片
     *
     * **/
    int addsTemplate(List<KzSpuTemplatelBo> list);
}
