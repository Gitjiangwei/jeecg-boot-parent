package org.kunze.diansh.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.Category;
import org.kunze.diansh.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Api(tags = "商品分类")
@RestController
@RequestMapping(value = "/kunze/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;


    /**
     * 查询商品分类菜单
     *
     * @param pid 父级id 如果是顶级类则为0，默认为0
     * @return
     */
    @ApiOperation("商品分类菜单接口")
    @AutoLog("查询商品分类")
    @GetMapping(value = "/qryList")
    public @ResponseBody Result<List<Category>> qryLists(@RequestParam(required = false, defaultValue = "0") String pid,
                                    @RequestParam(required = false) String id) {
        Result<List<Category>> result = new Result<>();
        List<Category> categoryList = categoryService.qryList(pid, id);
   /*     if(CollectionUtils.isEmpty(categoryList)){
            throw new UdaiException(ExceptionEnums.CATEGORY_NOT_FIND);
        }*/
        result.setResult(categoryList);
        result.setSuccess(true);
        return result;
    }


    /**
     * 添加商品分类
     *
     * @param category
     * @return
     */
    @ApiOperation("商品分类添加接口")
    @AutoLog("添加商品分类")
    @PostMapping(value = "/categorySave")
    public Result<Category> saveCategory(@RequestBody Category category) {
        Result<Category> result = new Result<Category>();
        if (category == null) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = categoryService.saveCategory(category);
            if (resultOk) {
                result.success("添加成功");
            } else {
                result.error500("添加失败！");
            }
        }
        return result;
    }

    @ApiOperation("商品分类修改接口")
    @AutoLog("修改商品分类")
    @PostMapping(value = "/categoryUpdate")
    public Result<Category> updateCategory(@RequestBody Category category) {
        Result<Category> result = new Result<Category>();
        if (category.getId() == null || category.getId().equals("")) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = categoryService.updateCategory(category);
            if (resultOk) {
                result.success("修改成功");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /**
     * 批量删除
     *
     * @param ids 字符串类型为"1,2,3",这样拼接起来的
     * @return
     */
    @ApiOperation("商品分类批量删除接口")
    @AutoLog("批量删除商品分类")
    @DeleteMapping(value = "/categoryDels")
    public Result<Category> delCategorys(@RequestParam(name = "ids") String ids) {
        Result<Category> result = new Result<Category>();
        if (ids == null || ids.equals("")) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = categoryService.deleteCategorys(ids);
            if (resultOk) {
                result.success("删除成功");
            } else {
                result.error500("删除失败！");
            }
        }
        return result;
    }


    /**
     * 商品分类删除
     *
     * @param id 商品id
     * @return
     */
    @ApiOperation("商品分类删除接口")
    @AutoLog("商品分类删除")
    @DeleteMapping(value = "/categoryDel")
    public Result<Category> delCategory(@RequestParam(name = "id") String id) {
        Result<Category> result = new Result<Category>();
        if (id == null || id.equals("")) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = categoryService.deleteCategory(id);
            if (resultOk) {
                result.success("删除成功");
            } else {
                result.error500("删除失败！");
            }
        }
        return result;
    }



    @ApiOperation("模糊查询分类")
    @AutoLog("模糊查询分类")
    @PostMapping(value = "/qryCategoryByName")
    public Result<List<Category>> qryCategoryByName(@RequestParam(name = "name") String name){
        Result<List<Category>> result = new Result<List<Category>>();
        if(null != name && !"".equals(name)){
            List<Category> list = categoryService.qryCategoryByName(name);
            result.setSuccess(true);
            result.setResult(list);
        }else{
            result.error500("名字不能为空！");
        }
        return result;
    }
}
