package org.kunze.diansh.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Category;
import org.kunze.diansh.entity.CategoryHotel;
import org.kunze.diansh.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;


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
     * @return
     */
    @ApiOperation("商品分类菜单接口")
    @AutoLog("查询商品分类")
    @PostMapping(value = "/qryList")
    public @ResponseBody Result<Collection<Category>> qryList(@RequestParam(name = "cateId")String cateId) {
        Result<Collection<Category>> result = new Result<>();
        if(EmptyUtils.isEmpty(cateId)){
            cateId = "0";
        }
        try {
            Collection<Category> categoryList = categoryService.getAllCategory(cateId);
            result.setResult(categoryList);
            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            result.error500("获取菜单时出现错误！");
        }
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

    @ApiOperation("查询全部一级分类ID")
    @GetMapping(value = "/queryCid1")
    public Result<PageInfo<Map<String,String>>> queryCid1(@RequestParam(name = "name",required = false) String name,
                                                            @RequestParam(name = "pageNo") String pageNo,
                                                            @RequestParam(name = "pageSize") String pageSize){
        Result<PageInfo<Map<String,String>>> result = new Result<PageInfo<Map<String, String>>>();
        PageInfo<Map<String,String>> mapPageInfo = categoryService.queryCid1(name,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        result.setSuccess(true);
        result.setResult(mapPageInfo);
        return result;
    }

    @ApiOperation("获取分类 通过店铺id 类型为饭店分类菜单")
    @PostMapping(value = "/getHotelMenu")
    public Result getHotelMenu(@RequestParam(name = "shopId") String shopId,
                               @RequestParam(name = "isShow") String isShow){
        Result result = new Result();
        if(EmptyUtils.isEmpty(shopId)){
            return result.error500("参数不能为空！");
        }
        if(EmptyUtils.isEmpty(isShow)){
            return result.error500("参数不能为空！");
        }
        Collection<CategoryHotel> hotelList = categoryService.getHotelCategoryByShopId(shopId,isShow);
        result.setResult(hotelList);
        return result;
    }

    @ApiOperation("添加分类 分类类型为餐饮分类")
    @PostMapping(value = "/addCategoryHotel")
    public Result addCategoryHotel(@RequestBody @Valid CategoryHotel categoryHotel, BindingResult bindingResult){
        Result result = new Result();
        //参数校验
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1,m2)->","+m2)
                    .orElse("输入参数有误！");
            throw new IllegalArgumentException(messages);
        }
        Boolean resultFlag = categoryService.addCategoryHotel(categoryHotel);
        if(resultFlag){
            result.success("添加成功!");
        }else {
            result.error500("添加失败!");
        }
        return result;
    }

    @ApiOperation("修改分类 分类类型为餐饮分类")
    @PostMapping(value = "/updateCategoryHotel")
    public Result updateCategoryHotel(@RequestBody CategoryHotel categoryHotel){
        Result result = new Result();
        if (EmptyUtils.isEmpty(categoryHotel.getId())){
            return result.error500("id is not null!");
        }
        Boolean resultFlag = categoryService.updateCategoryHotel(categoryHotel);
        if(resultFlag){
            result.success("修改成功!");
        }else {
            result.error500("修改失败!");
        }
        return result;
    }

    @ApiOperation("删除分类 通过id 分类类型为餐饮分类")
    @PostMapping(value = "/delCategoryHotelById")
    public Result delCategoryHotelById(@RequestParam(name = "id") String id){
        Result result = new Result();
        if (EmptyUtils.isEmpty(id)){
            return result.error500("id is not null!");
        }
        Boolean resultFlag = categoryService.delCategoryHotelById(id);
        if(resultFlag){
            result.success("删除成功!");
        }else {
            result.error500("删除失败!");
        }
        return result;
    }
}
