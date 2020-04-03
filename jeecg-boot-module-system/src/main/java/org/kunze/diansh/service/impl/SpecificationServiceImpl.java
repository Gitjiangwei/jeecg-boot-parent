package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.entity.Specification;
import org.kunze.diansh.mapper.SpecificationMapper;
import org.kunze.diansh.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper,Specification> implements ISpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;



    /**
     * 查询商品规格参数模板，json格式。
     *
     * @param categoryId
     * @return
     */
    @Override
    public Specification qrySpecification(String categoryId) {
        Specification specification = new Specification();
        if(categoryId==null||categoryId.equals("")){
            return specification;
        }else{
            specification =  specificationMapper.qrySpecification(categoryId);
        }
        return specification;
    }

    /**
     * 添加商品规格参数模板，specificaions参数为json格式
     *
     * @param
     * @return ok:添加成功；error：添加失败；NOT：参数为空
     */
    @Override
    public String saveSpecification(String categoryId,String specifications) {
        String success = "NOT";
        if((specifications!=null && !specifications.equals(""))&&(categoryId!=null && !categoryId.equals(""))) {
            Specification specification = specification(categoryId,specifications);
            int result = specificationMapper.saveSpecification(specification);
            if (result > 0) {
                success = "ok";
            }else{
                success = "error";
            }
        }
        return success;
    }

    /**
     * 修改商品规格参数模板，specificaions参数为json格式
     *
     * @param categoryId     分类id
     * @param specifications 参数为json格式
     * @return 添加成功；error：添加失败；NOT：参数为空
     */
    @Override
    public String updateSpecification(String categoryId, String specifications) {
        String success = "NOT";
        if((specifications!=null && !specifications.equals(""))&&(categoryId!=null && !categoryId.equals(""))) {
            Specification specification = specification(categoryId,specifications);
            int result = specificationMapper.updateSpecification(specification);
            if (result > 0) {
                success = "ok";
            }else{
                success = "error";
            }
        }
        return success;
    }

    /**
     * 删除商品规格参数模板
     *
     * @param categoryIds
     * @return
     */
    @Override
    public Boolean delSpecification(String categoryIds) {
        Boolean flag = false;
        List<String> categoryList = new ArrayList<String>();
        if(categoryIds.contains(",")){
            categoryList = new ArrayList<String>(Arrays.asList(categoryIds.split(",")));
        }else {
            categoryList.add(categoryIds);
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userName = "";
        if(sysUser!=null){
            userName = sysUser.getRealname();
        }
        int result = specificationMapper.delSpecifications(userName,categoryList);
        if(result>0){
            flag = true;
        }
        return flag;
    }


    /**
     * 将内容放在模板对象中
     * @param categoryId
     * @param specifications
     * @return
     */
    private Specification specification(String categoryId, String specifications){
        Specification specification = new Specification();
        specification.setCategoryId(categoryId);
        specification.setSpecifications(specifications);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser!=null){
            specification.setUpdateName(sysUser.getRealname());
        }else {
            specification.setUpdateName("");
        }
        return specification;
    }


}
