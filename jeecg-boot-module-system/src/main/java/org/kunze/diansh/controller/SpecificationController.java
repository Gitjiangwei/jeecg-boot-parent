package org.kunze.diansh.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.SpecificationVo;
import org.kunze.diansh.entity.Specification;
import org.kunze.diansh.service.ISpecificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@Api(tags = "商品规格参数模板")
@Slf4j
@RestController
@RequestMapping(value = "/kunze/spec")
public class SpecificationController {


    @Autowired
    private ISpecificationService specificationService;


    @ApiOperation("商品规格参数模板查询")
    @AutoLog("查询商品规格参数模板")
    @GetMapping(value = "/specList")
    public Result<SpecificationVo> qrySpecification(@RequestParam(name = "categoryId", required = false) String categoryId) {
        Result<SpecificationVo> result = new Result<SpecificationVo>();
        if (categoryId == null || categoryId.equals("")) {
            result.error500("参数丢失！");
            result.setSuccess(false);
            return result;
        }
        Specification specification = specificationService.qrySpecification(categoryId);
        if (specification == null) {
            result.error500("该分类还没有规格参数");
            result.setSuccess(false);
        } else {
            SpecificationVo specificationVo = new SpecificationVo();
            BeanUtils.copyProperties(specification, specificationVo);
            result.setResult(specificationVo);
            result.setSuccess(true);
        }
        return result;
    }


    @ApiOperation("商品规格参数模板添加")
    @AutoLog("添加商品规格参数模板")
    @PostMapping(value = "/saveSpec")
    public Result<T> saveSpecification(@RequestParam(name = "categoryId") String categoryId,
                                       @RequestParam(name = "specifications", required = false) String specifications) {
        Result<T> result = new Result<T>();
        if ((specifications != null && !specifications.equals("")) && (categoryId != null && !categoryId.equals(""))) {
            //ok:添加成功；error：添加失败；NOT：参数为空
            String resultOk = specificationService.saveSpecification(categoryId, specifications);
            if (resultOk.equals("ok")) {
                result.success("添加成功");
            } else if (resultOk.equals("error")) {
                result.success("添加失败！");
            } else if (resultOk.equals("NOT")) {
                result.success("参数模板内容为空");
            }
        } else {
            result.success("参数模板内容为空");
        }
        return result;
    }

    @ApiOperation("商品规格参数模板修改")
    @AutoLog("修改商品规格参数模板")
    @PostMapping(value = "/updateSpec")
    public Result<T> updateSpecification(@RequestParam(name = "categoryId") String categoryId,
                                         @RequestParam(name = "specifications", required = false) String specifications) {
        Result<T> result = new Result<T>();
        if ((specifications != null && !specifications.equals("")) && (categoryId != null && !categoryId.equals(""))) {
            //ok:添加成功；error：添加失败；NOT：参数为空
            String resultOk = specificationService.updateSpecification(categoryId, specifications);
            if (resultOk.equals("ok")) {
                result.success("修改成功");
            } else if (resultOk.equals("error")) {
                result.success("修改失败！");
            } else if (resultOk.equals("NOT")) {
                result.success("参数模板内容为空");
            }
        } else {
            result.success("参数模板内容为空");
        }
        return result;
    }


    @ApiOperation("商品规格参数模板删除")
    @AutoLog("删除商品规格参数模板")
    @DeleteMapping(value = "/delSpec")
    public Result<T> delSpecification(@RequestParam(name = "categoryId") String categoryId) {
        Result<T> result = new Result<T>();
        if (categoryId == null || categoryId.equals("")) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = specificationService.delSpecification(categoryId);
            if (resultOk) {
                result.success("删除成功！");
            } else {
                result.error500("删除失败！");
            }
        }
        return result;
    }

    @ApiOperation("商品规格参数模板批量删除【以作备用】")
    @AutoLog("批量删除商品规格参数模板")
    @DeleteMapping(value = "/delSpecs")
    public Result<T> delSpecifications(@RequestParam(name = "categoryIds") String categoryIds) {
        Result<T> result = new Result<T>();
        if (categoryIds == null || categoryIds.equals("")) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = specificationService.delSpecification(categoryIds);
            if (resultOk) {
                result.success("删除成功！");
            } else {
                result.error500("删除失败！");
            }
        }
        return result;
    }
}
