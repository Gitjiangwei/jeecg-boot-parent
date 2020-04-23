package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.C;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.Commodity;
import org.kunze.diansh.salesTicket.SalesTicket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/dayin")
    public Result<T> intimiDate(){
        Result<T> result = new Result<T>();
        List<Commodity> list = new ArrayList<>();
        for(int i =0; i<5;i++){
            Commodity commodity = new Commodity("苹果","5","10","50");
            list.add(commodity);
        }
        printSale(list,"123456","金威超市","12547","5","500","450","50");
        return result;
    }





    /**
     * 商品信息
     * @param commodities
     * 打印销售小票
     *
     * @param order
     *            订单号
     *
     * @param cashier 收银员编号
     * @param num
     *            数量
     * @param sum
     *            总金额
     * @param practical
     *            实收
     * @param change
     *            找零
     */
    private void printSale(List<Commodity> commodities,String order, String shopName, String cashier, String num, String sum, String practical, String change){
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

            book.append(new SalesTicket(new ArrayList<>(commodities),shopName ,cashier, num, sum, practical, change,order), pageFormat);

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
