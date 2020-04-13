package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.controller.vo.BrandVo;
import org.kunze.diansh.entity.Brand;
import org.kunze.diansh.entity.CategoryBrand;
import org.kunze.diansh.entity.modelData.BrandModel;
import org.kunze.diansh.mapper.BrandMapper;
import org.kunze.diansh.mapper.CategoryBrandMapper;
import org.kunze.diansh.service.IBrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;


    /**
     * 查询商品品牌
     * @param brandModel
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<BrandModel> qryBrand(BrandModel brandModel, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BrandModel> brandModelList = brandMapper.qryBrand(brandModel);
        return new PageInfo<BrandModel>(brandModelList);
    }

    /**
     * 添加商品品牌
     *
     * @param brandVo@return
     */
    @Override
    public Boolean saveBrand(BrandVo brandVo) {
        Boolean flag = false;
        if(brandVo != null){
            Brand brand = new Brand();
            BeanUtils.copyProperties(brandVo,brand);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if(sysUser!=null){
                brand.setUpdateName(sysUser.getRealname());
            }else {
                brand.setUpdateName("");
            }
            brand.setId(UUID.randomUUID().toString().replace("-",""));
            int resultBrand = brandMapper.saveBrand(brand);
            if(resultBrand > 0){
                if(brandVo.getKId()!=null && !brandVo.getKId().equals("")) {
                   flag = saveCategoryBrand(brandVo.getKId(),brand.getId());
                }

            }
        }
        return flag;
    }

    /**
     * 修改商品品牌
     *
     * @param brandVo
     * @return
     */
    @Override
    public Boolean updateBrand(BrandVo brandVo) {
        Boolean flag = false;
        if(brandVo == null){
            return false;
        }
        if(brandVo.getId()!=null && !brandVo.getId().equals("")){
            Brand brand = new Brand();
            BeanUtils.copyProperties(brandVo,brand);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if(sysUser!=null){
                brand.setUpdateName(sysUser.getRealname());
            }else {
                brand.setUpdateName("");
            }
            int delResult = categoryBrandMapper.updateCategoryBrand(brand.getId());
            if(delResult > 0){
                int updateResult = brandMapper.updateBrand(brand);
                if(updateResult>0){
                    if(brandVo.getKId()!=null && !brandVo.getKId().equals("")) {
                        flag = saveCategoryBrand(brandVo.getKId(),brand.getId());
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 删除商品品牌
     *
     * @param bids
     * @return
     */
    @Override
    public Boolean delBrands(String bids) {
        Boolean flag = false;
        if(bids!=null && !"".equals(bids)){
            List<String> delList = new ArrayList<String>();
            if(bids.contains(",")) {
                 delList = new ArrayList<String>(Arrays.asList(bids.split(",")));
            }else{
                delList.add(bids);
            }
            int result = brandMapper.delBrands(delList);
            if(result>0){
                int delCategoryBrand = categoryBrandMapper.delCategoryBrand(delList);
                if(delCategoryBrand>0){
                    flag = true;
                }
            }
        }

        return flag;
    }

    /**
     * 查询品牌是否被引用，引用的品牌不能被删除
     *
     * @param brandId 品牌Id
     * @return true：可以删除，false：不可以删除
     */
    @Override
    public Boolean qryIsFlag(String brandId) {
        Boolean isflag = false;
        if(brandId!=null || !brandId.equals("")){
            List<String> stringList = new ArrayList<String>();
            if(brandId.contains(",")){
                stringList = new ArrayList<String>(Arrays.asList(brandId.split(",")));
            }else{
                stringList.add(brandId);
            }
            int result = brandMapper.qryIsFlag(stringList);
            if(result < 1){
                isflag = true;
            }
        }
        return isflag;
    }

    /**
     * 添加商品分类和品牌的中间表
     * @param kid 商品分类id
     * @param bid 商品品牌Id
     * @return
     */
    private Boolean saveCategoryBrand(String kid,String bid){
        Boolean flag = false;
        List<String> strList = new ArrayList<String>();
        if(kid.contains(",")) {
             strList = new ArrayList<String>(Arrays.asList(kid.split(",")));
        }else{
            strList.add(kid);
        }
        List<CategoryBrand> categoryBrandList = new ArrayList<CategoryBrand>();
        for(String item:strList){
            CategoryBrand categoryBrand = new CategoryBrand();
            categoryBrand.setBrandId(bid);
            categoryBrand.setCategoryId(item);
            categoryBrandList.add(categoryBrand);
        }
        int resultCateBrand = categoryBrandMapper.saveCategoryBrand(categoryBrandList);
        if(resultCateBrand>0){
            flag = true;
        }
        return flag;
    }
}
