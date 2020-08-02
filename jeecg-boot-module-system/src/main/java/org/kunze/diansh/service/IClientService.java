package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.entity.Client;


public interface IClientService extends IService<Client> {


    /**
     * 查询客户端信息
     * @param pageNo
     * @param pageSize
     * @return
     */

    PageInfo<Client> qryClientList( Integer pageNo, Integer pageSize);

      /**
       * 添加客户端信息
       * @param
       * @return
       * **/


   int addClient(Client client);


    /**
     * 安卓查询
     * @return
     */
   Client qryClient(String client);

    /**
     * 修改客户端信息
     * @param
     * @return
     * **/


    int updateClient(Client client);


    /**
     * 删除客户端信息
     * @param
     * @return
     * **/


    int deleteClient(Integer id);
}
