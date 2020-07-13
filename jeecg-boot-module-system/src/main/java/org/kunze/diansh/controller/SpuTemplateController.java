package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.KzSpuTemplatelBo;
import org.kunze.diansh.entity.modelData.KzSpuTemplateModel;
import org.kunze.diansh.service.IKzSpuTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品图片上传模块")
@Slf4j
@RestController
@RequestMapping(value = "/kunze/spu/template")
public class SpuTemplateController {


    @Autowired
    private IKzSpuTemplateService kzSpuTemplateService;

    @ApiOperation("根据分类ID查询对应图片")
    @AutoLog("根据分类ID查询对应图片")
    @GetMapping(value = "/list")

    public Result<PageInfo<KzSpuTemplateModel> >qryTemplateListById(String cid,
                                                           @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize") Integer pageSize)
    {

        Result<PageInfo<KzSpuTemplateModel>> result = new Result<PageInfo<KzSpuTemplateModel>>();
        if(cid.equals("")){
            result.error500("分类ID不能为空!");
        }else {
            PageInfo<KzSpuTemplateModel> spuModelPageInfo = kzSpuTemplateService.qryTemplateListById(cid, pageNo, pageSize);
            result.setSuccess(true);
            result.setResult(spuModelPageInfo);
        }
        return result;
    }




    @ApiOperation("批量添加图片")
    @AutoLog("批量添加图片")
    @PostMapping(value = "/add")
    public  Result  addsTemplate(@RequestBody List<KzSpuTemplateModel> kzSpuTemplatelBo)
    {

        Result<KzSpuTemplateModel> result = new Result<KzSpuTemplateModel>();
        int resultFlag = kzSpuTemplateService.addsTemplate(kzSpuTemplatelBo);
        if(resultFlag>0){
            result.success("add success！");
        }else{
            result.error500("add fail！");
        }
        return result;
    }

}
