package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.util.TreeUtil;
import org.kunze.diansh.entity.Category;
import org.kunze.diansh.entity.CategoryHotel;
import org.kunze.diansh.entity.Collect;
import org.kunze.diansh.mapper.CategoryHotelMapper;
import org.kunze.diansh.mapper.NewCategoryMapper;
import org.kunze.diansh.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CategoryServiceImpl extends ServiceImpl<NewCategoryMapper,Category> implements ICategoryService {

    @Autowired
    private NewCategoryMapper newCategoryMapper;

    @Autowired
    private CategoryHotelMapper categoryHotelMapper;

    @Override
    public List<Category> qryList(String pid,String id) {
        Category category = new Category();
  /*      if(StringUtils.isEmpty(pid)){
            throw new UdaiException(ExceptionEnums.ID_NULL);
        }*/
        category.setParentId(pid);
        if(id!=null&&!id.equals("")){
            category.setId(id);
        }else{
            category.setId(null);
        }
        if(pid!=null&&!pid.equals("")){
            category.setParentId(pid);
        }else{
            category.setParentId("0");
        }

        List<Category> list = new ArrayList<Category>();
        try {
            list = newCategoryMapper.qryCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 添加商品分类接口
     *
     * @param category
     * @return
     */
    @Override
    public Boolean saveCategory(Category category) {
        Boolean isflag = false;
        if (category!=null){
            category.setId(UUID.randomUUID().toString().replace("-",""));
            int result = newCategoryMapper.saveCategory(category);
            if(result>0){
                isflag = true;
            }
        }
        return isflag;
    }

    /**
     * 商品分类修改接口
     *
     * @param category
     * @return
     */
    @Override
    public Boolean updateCategory(Category category) {
        Boolean isflag = false;
        if(category.getId()!=null && !category.getId().equals("")){
            int result = newCategoryMapper.updateCategory(category);
            if(result>0){
                isflag = true;
            }
        }

        return isflag;
    }

    /**
     * 商品分类批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteCategorys(String ids) {
        Boolean isflag = false;
        if(ids != null && !ids.equals("")){
            List<String> delList = Arrays.asList(ids.split(","));
            int result = newCategoryMapper.deleteCategorys(delList);
            if(result>0){
                isflag = true;
            }
        }
        return isflag;
    }

    /**
     * 商品分类单个删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteCategory(String id) {
        Boolean isflag = false;
        if(id != null && !id.equals("")){
            int result = newCategoryMapper.deleteCategory(id);
            if(result>0){
                isflag = true;
            }
        }
        return isflag;
    }

    /**
     * 根据分类名称 模糊查询相关分类
     * @param name 分类名称
     * @return
     */
    @Override
    public List<Category> qryCategoryByName(String name) {
        List<Category> list = new ArrayList<Category>();
        try{
            list = newCategoryMapper.qryCategoryByName(name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取分类的全部数据
     * @param cateId 分类id默认为 0
     * @return
     */
    public Collection<Category> getAllCategory(String cateId){
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_flag","0")
                .orderBy(true,true,"sort");
        List<Category> categoryList = newCategoryMapper.selectList(queryWrapper);
        Collection collection=TreeUtil.toTree(categoryList,"id","parentId","childrenList",Category.class,cateId);

        return collection;
    }

    /**
     * 查询全部一级分类
     *
     * @param name
     * @return
     */
    @Override
    public PageInfo<Map<String, String>> queryCid1(String name,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<Map<String,String>> mapList = newCategoryMapper.queryCid1(name);
        PageInfo<Map<String,String>> mapPageInfo = new PageInfo<Map<String,String>>(mapList);
        mapPageInfo.setTotal(page.getTotal());
        return mapPageInfo;
    }

    /**
     * 获取分类 通过店铺id 类型为餐饮分类菜单
     * @param shopId 店铺id
     * @return
     */
    @Override
    public Collection<CategoryHotel> getHotelCategoryByShopId(String shopId,String isShow){
        QueryWrapper<CategoryHotel> hotelQueryWrapper = new QueryWrapper<>();
        hotelQueryWrapper.eq("del_flag",0)
                .eq("shop_id",shopId);
        if ("true".equals(isShow)){
            hotelQueryWrapper.eq("`show`",1);
        }
        List<CategoryHotel> hotelList = categoryHotelMapper.selectList(hotelQueryWrapper);
        Collection collection = TreeUtil.toTree(hotelList,"id","pid","childrenList",CategoryHotel.class,null);
        return collection;
    }


    /**
     * 添加分类 分类类型为餐饮分类
     * @param categoryHotel
     * @return
     */
    @Override
    public Boolean addCategoryHotel(CategoryHotel categoryHotel){
        Integer row = categoryHotelMapper.addCategoryHotel(categoryHotel);
        if(row > 0){
            return true;
        }
        return false;
    }

    /**
     * 修改分类 分类类型为餐饮分类
     * @param categoryHotel
     * @return
     */
    @Override
    public Boolean updateCategoryHotel(CategoryHotel categoryHotel){
        Integer row = categoryHotelMapper.updateCategoryHotel(categoryHotel);
        if(row > 0){
            return true;
        }
        return false;
    }

    /**
     * 删除分类 通过id
     * @param id
     * @return
     */
    @Override
    public Boolean delCategoryHotelById(String id){
        Integer row = categoryHotelMapper.delCategoryHotelById(id);
        if(row > 0){
            return true;
        }
        return false;
    }

}
