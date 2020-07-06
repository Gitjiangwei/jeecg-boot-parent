package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.HomePage;

import java.util.List;

public interface IHomePageService extends IService<HomePage> {

    /**
     * 添加分类专区
     * @param homePage
     * @return
     */
    Boolean insertHomePage(HomePage homePage);

    /**
     * 修改分类专区
     * @param homePage
     * @return
     */
    Boolean updateHomePage(HomePage homePage);

    /**
     * 查询分类专区
     */
    PageInfo<HomePage> queryHomePage(String homeName,Integer pageNo,Integer pageSize);


    /**
     * 删除分类专区
     * @param ids
     * @return
     */
    Boolean delHomgPage(String ids);
}
