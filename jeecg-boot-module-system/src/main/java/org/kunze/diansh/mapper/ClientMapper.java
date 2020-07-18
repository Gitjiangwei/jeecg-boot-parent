package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Client;

import java.util.List;


public interface ClientMapper extends BaseMapper<Client> {


    /**
     * 根据商品ID查询对应图片
     * @param
     * @return
     */

    List<Client> qryClientList();


    /**
     *
     * 添加客户端信息
     * **/
    int addClient(@Param("client") Client client);


    /**
     *
     * 修改客户端信息
     * **/
    int updateClient(@Param("client") Client client);

    /**
     *
     * 删除客户端信息
     * **/
    int deleteClient(@Param("id") Integer id);

}
