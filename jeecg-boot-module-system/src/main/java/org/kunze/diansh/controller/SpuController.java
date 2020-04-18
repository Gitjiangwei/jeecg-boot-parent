package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.controller.vo.SpuVo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.modelData.SpuModel;
import org.kunze.diansh.service.ISpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制层
 */
@Api(tags = "商品模块")
@Slf4j
@RestController
@RequestMapping(value = "/kunze/spu")
public class SpuController {

    @Autowired
    private ISpuService spuService;


    @ApiOperation("商品查询")
    @AutoLog("查询商品")
    @GetMapping(value = "/spuList")
    public Result<PageInfo<SpuModel>> qrySpuList(SpuVo spuVo,
                                                 @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<SpuModel>> result = new Result<PageInfo<SpuModel>>();
        PageInfo<SpuModel> spuModelPageInfo = spuService.qrySpuList(spuVo,pageNo,pageSize);
        result.setSuccess(true);
        result.setResult(spuModelPageInfo);
        return result;
    }


    @ApiOperation("商品品牌查询，下拉框使用")
    @GetMapping(value = "/qrySpuBrand")
    public Result<List<SpuBrandVo>> qrySpuBrand(){
        Result<List<SpuBrandVo>> result = new Result<List<SpuBrandVo>>();
        List<SpuBrandVo> spuBrandVoList = spuService.qrySpuBrand();
        result.setResult(spuBrandVoList);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("根据商品分类查询商品品牌")
    @GetMapping(value = "/qrySpuBrands")
    public Result<List<SpuBrandVo>> qrySpuBrands(@RequestParam(name = "categoryId",required = false) String categoryId){
        Result<List<SpuBrandVo>> result = new Result<List<SpuBrandVo>>();
        List<SpuBrandVo> spuBrandVoList = spuService.qrySpuBrands(categoryId);
        result.setResult(spuBrandVoList);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("商品添加")
    @AutoLog("添加商品")
    @PostMapping(value = "/saveGood")
    public Result<SpuBo> saveSpu(@RequestBody SpuBo spuBo){
        Result<SpuBo> result = new Result<SpuBo>();
        Boolean resultOk = spuService.saveSpu(spuBo);
        if(resultOk){
            result.success("添加成功");
        }else{
            result.success("添加失败！");
        }
        return result;
    }

    @ApiOperation("修改商品信息")
    @AutoLog("更新商品信息")
    @PostMapping(value = "/updateSpu")
    public Result<SpuBo> updateSpu(@RequestBody SpuBo spuBo){
        Result<SpuBo> result = new Result<SpuBo>();
        Boolean resultFlag = spuService.updateSpu(spuBo);
        if(resultFlag){
            result.success("修改成功");
        }else{
            result.success("修改失败！");
        }
        return result;
    }

    @ApiOperation("删除商品信息")
    @AutoLog("删除商品信息")
    @PostMapping(value = "/deleteSpu")
    public Result<SpuBo> deleteSpu(@RequestBody SpuBo spuBo){
        Result<SpuBo> result = new Result<SpuBo>();
        Boolean resultFlag = spuService.deleteSpu(spuBo);

        return result;
    }

    @ApiOperation("通过商品分类Id查询相关商品的详细信息")
    @AutoLog("通过商品分类Id查询相关商品的详细信息")
    @PostMapping(value = "/querySpuByCateID")
    public Result<PageInfo<Sku>> querySpuByCateID(@RequestParam(name = "cateId") String cateId,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<Sku>> result = new Result<PageInfo<Sku>>();
        PageInfo<Sku> spuList = spuService.querySpuById(cateId,pageNo,pageSize);
        result.setResult(spuList);
        return result;
    }
}
