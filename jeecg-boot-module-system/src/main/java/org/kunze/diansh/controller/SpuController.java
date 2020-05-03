package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.BeSimilarSpuVo;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.controller.vo.SpuDetailVo;
import org.kunze.diansh.controller.vo.SpuVo;
import org.kunze.diansh.entity.Goods;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.SpuDetail;
import org.kunze.diansh.entity.modelData.SpuModel;
import org.kunze.diansh.esRepository.GoodsRepository;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuMapper;
import org.kunze.diansh.service.ISpuService;
import org.kunze.diansh.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
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
    public Result<PageInfo<SpuModel>> querySpuByCateID(String cateId,
                                                  @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<SpuModel>> result = new Result<PageInfo<SpuModel>>();
        PageInfo<SpuModel> spuList = spuService.querySpuById(cateId,pageNo,pageSize);
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
                                                       @RequestParam(value = "spuId") String spuId){
        Result<List<BeSimilarSpuVo>> result = new Result<List<BeSimilarSpuVo>>();
        if(cid3 == null || cid3.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else if(spuId == null || spuId.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else {
            List<BeSimilarSpuVo> beSimilarSpuVoList = spuService.selectBySimilarSpu(cid3,spuId);
            result.setSuccess(true);
            result.setResult(beSimilarSpuVoList);
        }
        return result;
    }

    @ApiOperation("首页查询分类商品")
    @AutoLog("首页查询分类商品")
    @PostMapping(value = "/categorySpu")
    public Result<List<BeSimilarSpuVo>> selectCategorySpu(@RequestParam(value = "cid3") String cid3){
        Result<List<BeSimilarSpuVo>> result = new Result<List<BeSimilarSpuVo>>();
        if(cid3 == null || cid3.equals("")){
            result.setSuccess(true);
            result.success("参数丢失！");
        }else {
            List<BeSimilarSpuVo> beSimilarSpuVoList = spuService.selectCategorySpu(cid3);
            result.setSuccess(true);
            result.setResult(beSimilarSpuVoList);
        }
        return result;
    }



    //测试插入
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @GetMapping(value = "/testInsert")
    public void testInsert(){
        JSONObject json = JSONObject.parseObject(getJson());

        JSONArray jsonArray = json.getJSONArray("data");


        Integer spuFlag = 1000;
        for(int i=0;i<jsonArray.size();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            Spu spu = new Spu();
            spuFlag++;
            spu.setId(spuFlag.toString());
            spu.setTitle(object.get("name").toString());
            spu.setImage(object.get("main_image").toString());
            spu.setCid3("877");
            spu.setCid2("872");
            spu.setCid1("871");
            spu.setBrandId("9637");
            int rows = spuMapper.saveSpu(spu);
            List<Sku> skuList = new ArrayList<Sku>();
            if(rows != 0){

                Sku s = new Sku();
                s.setId(UUID.randomUUID().toString().replace("-",""));
                s.setSpuId(spu.getId());
                s.setTitle(object.get("name").toString());
                s.setImages(object.get("main_image").toString());
                s.setPrice("1000");
                s.setNewPrice("2000");
                s.setEnable("1");
                skuList.add(s);
                skuMapper.saveSku(skuList);
            }
        }
    }

    public static String getJson() {
        String jsonStr = "";
        try {
            File file = new File("D:\\opt\\分类\\分类\\粮油调味.json");
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file),"Utf-8");

            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            return null;
        }
    }
}
