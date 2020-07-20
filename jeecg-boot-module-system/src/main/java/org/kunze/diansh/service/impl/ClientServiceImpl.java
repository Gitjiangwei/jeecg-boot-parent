package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.kunze.diansh.entity.Client;
import org.kunze.diansh.mapper.ClientMapper;
import org.kunze.diansh.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client>  implements IClientService {


    @Autowired
    private ClientMapper clientMapper;

    @Override
    public PageInfo<Client> qryClientList(Integer pageNo, Integer pageSize) {

        Page page = PageHelper.startPage(pageNo, pageSize);
        List<Client> clientList = clientMapper.qryClientList();
        PageInfo pageInfo = new PageInfo<Client>(clientList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    @Transactional
    public int addClient(Client client) {
        try {
            return clientMapper.addClient(client);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("插入失败");
            return 0;
        }
    }

    /**
     * 安卓查询
     *
     * @param client
     * @return
     */
    @Override
    public Client qryClient(String client) {
        if (StringUtils.isEmpty(client)){
            return null;
        }else {
            Client client1 = clientMapper.qryClient(client);
            return client1;
        }
    }

    @Override
    public int updateClient(Client client) {
        try {
            return clientMapper.updateClient(client);
        } catch (Exception e) {
            log.error("修改失败");
            return 0;
        }
    }

    @Override
    public int deleteClient(Integer id) {
        try {
            return clientMapper.deleteClient(id);
        } catch (Exception e) {
            log.error("删除失败");
            return 0;
        }
    }
}