package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.BrandVo;
import org.kunze.diansh.entity.Brand;
import org.kunze.diansh.entity.modelData.BrandModel;
import org.kunze.diansh.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(tags = "商品品牌")
@RequestMapping(value = "/kunze/brand")
public class BrandController {


    @Autowired
    private IBrandService brandService;


    @ApiOperation("商品品牌查询")
    @AutoLog("商品品牌查询")
    @GetMapping(value = "/qryBrandList")
    public Result<PageInfo<BrandModel>> qryBrandList(BrandModel brand, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize") Integer pageSize) {
        Result<PageInfo<BrandModel>> result = new Result<>();
        PageInfo<BrandModel> brandPageInfo = brandService.qryBrand(brand, pageNo, pageSize);
        result.setSuccess(true);
        result.setResult(brandPageInfo);
        return result;
    }

    @ApiOperation("商品品牌添加")
    @AutoLog("商品品牌添加")
    @PostMapping(value = "/saveBrand")
    public Result<BrandVo> saveBrand(@RequestBody BrandVo brandVo) {
        Result<BrandVo> result = new Result<BrandVo>();
        if (brandVo != null) {
            if (brandVo.getLetter() != null && brandVo.getLetter().length() > 1) {
                result.error500("首字母只能输入一位字符！");
                return result;
            }
            Boolean resultOk = brandService.saveBrand(brandVo);
            if (resultOk) {
                result.success("添加成功");
            } else {
                result.error500("添加失败！");
            }
        } else {
            result.error500("参数丢失！");
        }
        return result;
    }

    @ApiOperation("商品品牌修改")
    @AutoLog("商品品牌修改")
    @PostMapping(value = "/updateBrand")
    public Result<BrandVo> updateBrand(@RequestBody BrandVo brandVo) {
        Result<BrandVo> result = new Result<BrandVo>();
        if (brandVo.getId() == null || brandVo.getId().equals("")) {
            result.error500("参数丢失！");
        } else {
            if (brandVo.getLetter() != null && brandVo.getLetter().length() > 1) {
                result.error500("首字母只能输入一位字符！");
                return result;
            }
            Boolean resultOk = brandService.updateBrand(brandVo);
            if (resultOk) {
                result.success("修改成功");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    @ApiOperation("批量删除商品品牌")
    @AutoLog("批量删除商品品牌")
    @DeleteMapping(value = "/delBrands")
    public Result<BrandVo> delBrands(@RequestParam(name = "bids") String bids) {
        Result<BrandVo> result = new Result<BrandVo>();
        if (bids == null || bids.equals("")) {
            result.error500("参数丢失！");
        } else {
            //判断该品牌是否可以删除
            Boolean isFlagOk = brandService.qryIsFlag(bids);
            if (isFlagOk) {
                Boolean resultOk = brandService.delBrands(bids);
                if (resultOk) {
                    result.success("删除成功");
                } else {
                    result.error500("删除失败！");
                }
            } else {
                result.error500("您选择要删除包含还在使用的品牌，请重新选择！");
            }
        }
        return result;
    }

    @ApiOperation("删除商品品牌")
    @AutoLog("删除商品品牌")
    @DeleteMapping(value = "/delBrand")
    public Result<BrandVo> delBrand(@RequestParam(name = "bid") String bid) {
        Result<BrandVo> result = new Result<BrandVo>();
        if (bid == null || bid.equals("")) {
            result.error500("参数丢失！");
        } else {
            //判断该品牌是否可以删除
            Boolean isFlagOk = brandService.qryIsFlag(bid);
            if (isFlagOk) {
                Boolean resultOk = brandService.delBrands(bid);
                if (resultOk) {
                    result.success("删除成功");
                } else {
                    result.error500("删除失败！");
                }
            } else {
                result.error500("您要删除的品牌还在使用，不可以删除！");
            }
        }
        return result;
    }


}
