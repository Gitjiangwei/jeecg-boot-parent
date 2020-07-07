package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.HomePage;

import java.util.List;

public interface HomePageMapper extends BaseMapper<HomePage> {

    /**
     * 添加分类专区
     * @param homePage
     * @return
     */
    int insertHomePage(HomePage homePage);

    /**
     * 修改分类专区
     * @param homePage
     * @return
     */
    int updateHomePage(HomePage homePage);

    /**
     * 查询分类专区
     */
    List<HomePage> queryHomePage(HomePage homePage);


    /**
     * 删除分类专区
     * @param ids
     * @return
     */
    int delHomgPage(@Param("ids") List<String> ids);

    /**
     * 查询图片
     * @param id
     * @return
     */
    List<String> queryHomeImage(@Param("ids") List<String> ids);


    /**
     * 检索正在使用的专区
     * @param homePageIds
     * @return
     */
    int queryNotPage(@Param("list") List<String> homePageIds);
}
