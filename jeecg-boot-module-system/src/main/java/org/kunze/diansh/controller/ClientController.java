package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.Client;
import org.kunze.diansh.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "客户端信息")
@Slf4j
@RestController
@RequestMapping(value = "/kunze/client/")
public class ClientController {


    @Autowired
    private IClientService clientService;

    @ApiOperation("查询客户端信息")
    @AutoLog("查询客户端信息")
    @GetMapping(value = "/list")

    public Result<PageInfo<Client> >qryClientList(
                                                        @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize") Integer pageSize)
    {

        Result<PageInfo<Client>> result = new Result<PageInfo<Client>>();

            PageInfo<Client> spuModelPageInfo = clientService.qryClientList(pageNo, pageSize);
            result.setSuccess(true);
            result.setResult(spuModelPageInfo);
        return result;
    }


    @ApiOperation("添加客户端信息")
    @AutoLog("添加客户端信息")
    @PostMapping(value = "/add")

    public  Result  addClient(@RequestBody Client client)
    {

        Result<Client> result = new Result<Client>();
        int resultFlag = clientService.addClient(client);
        if(resultFlag>0){
            result.success("add success！");
        }else{
            result.error500("add fail！");
        }
        return result;
    }


    @ApiOperation("修改客户端信息")
    @AutoLog("修改客户端信息")
    @PostMapping(value = "/update")

    public  Result  updateClient(@RequestBody Client client)
    {

        Result<Client> result = new Result<Client>();
        int resultFlag = clientService.updateClient(client);
        if(resultFlag>0){
            result.success("update success！");
        }else{
            result.error500("update fail！");
        }
        return result;
    }


    @ApiOperation("删除客户端信息")
    @AutoLog("删除客户端信息")
    @PostMapping(value = "/delete")

    public  Result  deleteClient(@RequestBody Integer id)
    {

        Result<Client> result = new Result<Client>();
        int resultFlag = clientService.deleteClient(id);
        if(resultFlag>0){
            result.success("delete success！");
        }else{
            result.error500("delete fail！");
        }
        return result;
    }

    @ApiOperation("客户端查询版本号")
    @AutoLog("客户端查询版本号")
    @PostMapping(value = "/qryClient")
    public Result<Client> qryClient(@RequestParam(name = "client")String client){
        Result<Client> result = new Result<Client>();
        Client client1 = clientService.qryClient(client);
        result.setResult(client1);
        result.setSuccess(true);
        return result;
    }

}
