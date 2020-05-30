package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.C;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.DistributionVo;
import org.kunze.diansh.controller.vo.SalesTicketVo;
import org.kunze.diansh.entity.Commodity;
import org.kunze.diansh.salesTicket.SalesTicket;
import org.kunze.diansh.service.IIntimidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "打单机")
@RequestMapping(value = "/kunze/imidate")
public class IntimidateController {

    @Autowired
    private IIntimidateService intimidateService;


    @ApiOperation("打印接口【假数据】")
    @AutoLog("打印小票【假数据】")
    @PostMapping(value = "/dayinfalse")
    public Result<SalesTicketVo> intimiDate(@RequestParam(name = "pickUp") String pickUp){
        Result<SalesTicketVo> result = new Result<SalesTicketVo>();
/*        List<Commodity> list = new ArrayList<>();
        for(int i =0; i<salesTicketVo.getCommodityList().size();i++){
            Commodity commodity = new Commodity(salesTicketVo.getCommodityList().get(i).getSpuName(),salesTicketVo.getCommodityList().get(i).getUnitPrice(),
                    salesTicketVo.getCommodityList().get(i).getSpuNum(),salesTicketVo.getCommodityList().get(i).getUnitPriceTotle());
            list.add(commodity);
        }
        printSale(list,salesTicketVo);*/
        SalesTicketVo salesTicketVo = new SalesTicketVo();
        //salesTicketVo.setChanges("150");
        List<Commodity> commodities = new ArrayList<>();
        Commodity commodity = new Commodity("凯迪拉克","5","5000","25000");
        commodities.add(commodity);
        Commodity commodity1 = new Commodity("苹果(500克/盒)苹果(500克/盒)苹果(500克/盒)苹果(500克/盒)","10","5","50");
        commodities.add(commodity1);
        Commodity commodity2 = new Commodity("凯迪拉克(500克/盒)","5","5000","25000");
        commodities.add(commodity2);
        Commodity commodity3 = new Commodity("蓝莓(500克/盒)","10","5","50");
        commodities.add(commodity3);
        Commodity commodity4 = new Commodity("蓝莓(500克/盒)","10","5","50");
        commodities.add(commodity4);
        salesTicketVo.setCommodityList((ArrayList<Commodity>) commodities);
        DistributionVo distributionVo = new DistributionVo();
        distributionVo.setCall("姜先生");
        distributionVo.setContact("13023080927");
        if(pickUp.equals("2")) {
            salesTicketVo.setPickUp("商家配送");
            distributionVo.setShippingAddress("北京市海淀区西三旗街道永泰西里社区23幢2单元402室");
        }else {
            salesTicketVo.setPickUp("自提");
            distributionVo.setShippingAddress("");
        }
        salesTicketVo.setDistributionVo(distributionVo);
        salesTicketVo.setOrders("123456");
        salesTicketVo.setPractical("25000");
        salesTicketVo.setSaleNum("40");
        salesTicketVo.setSaleSum("25150");
        salesTicketVo.setShopAddress("长治市潞州区紫金西街5号");
        salesTicketVo.setShopName("金威超市");
        result.setSuccess(true);
        result.setResult(salesTicketVo);
        printSale(commodities,salesTicketVo);
        return result;
    }

    @ApiOperation("打印接口")
    @AutoLog("打印小票")
    @PostMapping(value = "/dayin")
    public Result<SalesTicketVo> intimiDateTrue(@RequestBody String orderId){
        Result<SalesTicketVo> result = new Result<SalesTicketVo>();
        JSONObject jsonObject = JSONObject.parseObject(orderId);
        orderId = jsonObject.get("orderId").toString();
        String status = jsonObject.get("status")==null?"":jsonObject.get("status").toString();
        if(orderId == null || orderId.equals("")){
            result.error500("参数丢失！");
        }else {
            SalesTicketVo salesTicketVo = intimidateService.selectSales(orderId,status);
            result.setSuccess(true);
            result.setResult(salesTicketVo);
        }
        return result;
    }

    /**
     * 商品信息
     */
    private void printSale(List<Commodity> list,SalesTicketVo salesTicketVo){
        try {
            // 通俗理解就是书、文档
            Book book = new Book();
            //设置成竖版
            PageFormat pageFormat = new PageFormat();
            pageFormat.setOrientation(PageFormat.PORTRAIT);

            // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
            Paper paper = new Paper();
            paper.setSize(158, 30000);// 纸张大小
            paper.setImageableArea(0, 0, 158, 30000);// A4(595 X
            // 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
            pageFormat.setPaper(paper);

            book.append(new SalesTicket(new ArrayList<>(list),salesTicketVo.getDistributionVo(),salesTicketVo.getShopName() ,
                    salesTicketVo.getSaleNum(), salesTicketVo.getSaleSum(),
                    salesTicketVo.getPractical(),salesTicketVo.getOrders(),
                    salesTicketVo.getShopAddress(),salesTicketVo.getPickUp()), pageFormat);

            // 获取打印服务对象
            PrinterJob job = PrinterJob.getPrinterJob();
            // 设置打印类
            job.setPageable(book);

            job.print();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
