package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.TreeUtil;
import org.kunze.diansh.entity.Category;
import org.kunze.diansh.entity.Collect;
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
     * @return
     */
    public Collection<Category> getAllCategory(){
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_flag","0")
                .orderBy(true,true,"sort");
        List<Category> categoryList = newCategoryMapper.selectList(queryWrapper);
        Collection collection=TreeUtil.toTree(categoryList,"id","parentId","childrenList",Category.class);

        return collection;
    }

}
