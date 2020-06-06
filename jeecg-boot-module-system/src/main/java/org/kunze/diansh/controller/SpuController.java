package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.OrderComsumer;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.jeecg.common.util.OrderCodeUtils;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.BeSimilarSpuVo;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.controller.vo.SpuDetailVo;
import org.kunze.diansh.controller.vo.SpuVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.entity.modelData.SpuModel;
import org.kunze.diansh.esRepository.GoodsRepository;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuMapper;
import org.kunze.diansh.service.ISpuService;
import org.kunze.diansh.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsRepository goodsRepository;

    @ApiOperation("商品查询")
    @AutoLog("查询商品")
    @GetMapping(value = "/spuList")
    public Result<PageInfo<SpuModel>> qrySpuList(SpuVo spuVo,
                                                 @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<SpuModel>> result = new Result<PageInfo<SpuModel>>();
        if(spuVo.getShopId()==null || spuVo.getShopId().equals("")){
            result.error500("超市唯一参数丢失！");
        }else {
            PageInfo<SpuModel> spuModelPageInfo = spuService.qrySpuList(spuVo, pageNo, pageSize);
            result.setSuccess(true);
            result.setResult(spuModelPageInfo);
        }
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
        if(spuBo.getShopId()==null || spuBo.getShopId().equals("")){
            result.error500("超市唯一参数丢失！");
        }else {
            Boolean resultOk = spuService.saveSpu(spuBo);
            if (resultOk) {
                result.success("添加成功");
            } else {
                result.success("添加失败！");
            }
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
    public Result<SpuBo> deleteSpu(@RequestBody JSONArray spuList){
        Result<SpuBo> result = new Result<SpuBo>();
        if(EmptyUtils.isEmpty(spuList)){
            return result.error500("参数不能为空！");
        }
        Boolean resultFlag = spuService.deleteSpu(spuList);
        if (resultFlag){
            result.setSuccess(true);
            result.setMessage("删除成功！");
        }else{
            result.setSuccess(false);
            result.setMessage("删除失败！");
        }
        return result;
    }

    @ApiOperation("通过商品分类Id查询相关商品的详细信息")
    @AutoLog("通过商品分类Id查询相关商品的详细信息")
    @PostMapping(value = "/querySpuByCateID")
    public Result<PageInfo<SpuModel>> querySpuByCateID(String cateId,
                                                  @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize") Integer pageSize,String shopId){
        Result<PageInfo<SpuModel>> result = new Result<PageInfo<SpuModel>>();
        PageInfo<SpuModel> spuList = spuService.querySpuById(cateId,pageNo,pageSize,shopId);
        result.setSuccess(true);
        result.setResult(spuList);
        return result;
    }


    @GetMapping(value = "/query")
    public void aa(){
        SpuBo spuBo = new SpuBo();
        spuBo.setId("10");
        spuBo.setCid1("74");
        spuBo.setCid2("75");
        spuBo.setCid3("76");
        int pageNo = 1;
        int pageSize = 100;
        int size = 100;
        do {
            SpuVo spuVo = new SpuVo();
            PageInfo<SpuBo> spuModelPageInfo = spuService.qrySpuLists(spuVo, pageNo, pageSize);
            List<SpuBo> spus = spuModelPageInfo.getList();
            List<Goods> goods = spus.stream().map(spu -> this.indexService.buildGoods(spu)).collect(Collectors.toList());
            //把goods放入索引库
            System.out.println(goods);
            this.goodsRepository.saveAll(goods);
            size = spus.size();
            pageNo ++;
        }while (size == 100);
    }

    @ApiOperation("前台商品详情查询")
    @AutoLog("前台商品详情查询")
    @PostMapping(value = "/spuDetail")
    public Result<SpuDetailVo> selectByPrimaryKey(@RequestParam(name = "spuId") String spuId){
        Result<SpuDetailVo> result = new Result<SpuDetailVo>();
        if(spuId==null||spuId.equals("")){
            result.setSuccess(false);
            result.success("参数丢失！");
        }else {
            SpuDetailVo spuDetailVo = spuService.selectByPrimaryKey(spuId);
            result.setSuccess(true);
            result.setResult(spuDetailVo);
        }
        return result;
    }

    @ApiOperation("前台查询相似商品")
    @AutoLog("前台查询相似商品")
    @PostMapping(value = "/simitSpu")
    public Result<List<BeSimilarSpuVo>> selectSimitSpu(@RequestParam(value = "cid3") String cid3,
                                                       @RequestParam(value = "spuId") String spuId,
                                                       @RequestParam(value = "shopId") String shopId){
        Result<List<BeSimilarSpuVo>> result = new Result<List<BeSimilarSpuVo>>();
        if(cid3 == null || cid3.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else if(spuId == null || spuId.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else {
            List<BeSimilarSpuVo> beSimilarSpuVoList = spuService.selectBySimilarSpu(cid3,spuId,shopId);
            result.setSuccess(true);
            result.setResult(beSimilarSpuVoList);
        }
        return result;
    }

    @ApiOperation("首页查询分类商品")
    @AutoLog("首页查询分类商品")
    @PostMapping(value = "/categorySpu")
    public Result<List<BeSimilarSpuVo>> selectCategorySpu(@RequestParam(value = "cid3",required = false) String cid3,
                                                          @RequestParam(value = "shopId",required = false) String shopId){
        Result<List<BeSimilarSpuVo>> result = new Result<List<BeSimilarSpuVo>>();
        if(cid3 == null || cid3.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else if(shopId == null || shopId.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else {
            List<BeSimilarSpuVo> beSimilarSpuVoList = spuService.selectCategorySpu(cid3,shopId);
            result.setSuccess(true);
            result.setResult(beSimilarSpuVoList);
        }
        return result;
    }

    @ApiOperation("更改商品上架状态（上架or下架）")
    @AutoLog("更改商品上架状态（上架or下架）")
    @PostMapping(value = "/updateSpuSaleable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "saleable",value = "是否上架 0下架/1上架"),
            @ApiImplicitParam(name = "shopId",value = "店铺id"),
            @ApiImplicitParam(name = "spuList",value = "商品id集合")
    })
    public Result<T> updateSpuSaleable(@RequestBody JSONObject params){
        Result<T> result = new Result<T>();
        String saleable = params.getString("saleable");
        String shopId = params.getString("shopId");
        List spuList = params.getJSONArray("spuList");


        if (EmptyUtils.isEmpty(saleable)){
            return result.error500("状态参数丢失！");
        }
        if (Integer.parseInt(saleable) > 1 || Integer.parseInt(saleable)<0){
            return result.error500("状态参数非法！");
        }
        if (EmptyUtils.isEmpty(spuList)){
            return result.error500("商品参数丢失！");
        }
        if (EmptyUtils.isEmpty(shopId)){
            return result.error500("店铺参数丢失！");
        }
        Boolean flag = spuService.updateSpuSaleable(saleable,spuList,shopId);
        if(flag){
            result.setSuccess(true);
            result.setMessage("上架成功！");
        }else {
            result.setSuccess(false);
            result.setMessage("上架时出现错误，请重试！");
        }
        return result;

    }

}
