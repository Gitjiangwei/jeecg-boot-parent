package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.C;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.SalesTicketVo;
import org.kunze.diansh.entity.Commodity;
import org.kunze.diansh.salesTicket.SalesTicket;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "打单机")
@RequestMapping(value = "/kunze/imidate")
public class IntimidateController {




    @ApiOperation("打印接口")
    @AutoLog("打印小票")
    @PostMapping(value = "/dayin")
    public Result<T> intimiDate(@RequestBody SalesTicketVo salesTicketVo){
        Result<T> result = new Result<T>();
        List<Commodity> list = new ArrayList<>();
        for(int i =0; i<salesTicketVo.getCommodityList().size();i++){
            Commodity commodity = new Commodity(salesTicketVo.getCommodityList().get(i).getSpuName(),salesTicketVo.getCommodityList().get(i).getUnitPrice(),
                    salesTicketVo.getCommodityList().get(i).getSpuNum(),salesTicketVo.getCommodityList().get(i).getUnitPriceTotle());
            list.add(commodity);
        }
        printSale(list,salesTicketVo);

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
                    salesTicketVo.getCashier(), salesTicketVo.getSaleNum(), salesTicketVo.getSaleSum(),
                    salesTicketVo.getPractical(), salesTicketVo.getChanges(),salesTicketVo.getOrders(),
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