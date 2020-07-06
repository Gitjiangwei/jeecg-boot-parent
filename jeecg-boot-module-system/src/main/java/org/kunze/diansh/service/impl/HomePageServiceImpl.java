package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DelFileUtils;
import org.kunze.diansh.entity.HomePage;
import org.kunze.diansh.mapper.HomePageMapper;
import org.kunze.diansh.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class HomePageServiceImpl extends ServiceImpl<HomePageMapper, HomePage> implements IHomePageService {

    @Autowired
    private HomePageMapper homePageMapper;


    /**
     * 添加分类专区
     *
     * @param homePage
     * @return
     */
    @Override
    public Boolean insertHomePage(HomePage homePage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean isFlag = false;
        String userName = "";//操作人
        if(sysUser!=null){
            userName = sysUser.getRealname();
        }
        homePage.setId(UUID.randomUUID().toString().replace("-",""));
        homePage.setUpdateName(userName);
        int result = homePageMapper.insertHomePage(homePage);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }

    /**
     * 修改分类专区
     *
     * @param homePage
     * @return
     */
    @Override
    public Boolean updateHomePage(HomePage homePage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean isFlag = false;
        String userName = "";//操作人
        if(sysUser!=null){
            userName = sysUser.getRealname();
        }
        homePage.setUpdateName(userName);
        List<String> list = new ArrayList<String>();
        list.add(homePage.getId());
        delFileImage(list);
        int result = homePageMapper.updateHomePage(homePage);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }

    /**
     * 查询分类专区
     *
     * @param homeName
     * @param pageNo
     * @param pageSize
     */
    @Override
    public PageInfo<HomePage> queryHomePage(String homeName, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        HomePage homePage = new HomePage();
        homePage.setHomgName(homeName);
        List<HomePage> homePageList = homePageMapper.queryHomePage(homePage);
        PageInfo<HomePage> pagePageInfo = new PageInfo<HomePage>(homePageList);
        page.setTotal(page.getTotal());
        return pagePageInfo;
    }

    /**
     * 删除分类专区
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean delHomgPage(String ids) {
        Boolean isFlag = false;
        if(ids != null && !ids.equals("")){
            char a = ids.charAt(ids.length()-1);
            if(a == ','){
                ids = ids.substring(0,ids.length()-1);
            }
            if(ids == null || ids.equals("")){
                return false;
            }
            List<String> stringList = new ArrayList<String>();
            if(ids.contains(",")){
                stringList = new ArrayList<String>(Arrays.asList(ids.split(",")));
            }else {
                stringList.add(ids);
            }
            delFileImage(stringList);
            int result = homePageMapper.delHomgPage(stringList);
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }


    /**
     * 删除原始附件
     * @param strings
     */
    private void delFileImage(List<String> strings){
        List<String> oldImage = homePageMapper.queryHomeImage(strings);
        if(oldImage != null){
            for (String item:oldImage){
                DelFileUtils.delFile(item);
            }
        }
    }
}
