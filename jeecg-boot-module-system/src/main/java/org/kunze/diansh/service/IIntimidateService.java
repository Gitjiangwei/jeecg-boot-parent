package org.kunze.diansh.service;

import org.kunze.diansh.controller.vo.SalesTicketVo;

public interface IIntimidateService {

    /***
     * 根据订单ID获取打印小票的数据
     * @param orderId 订单id
     * @return
     */
    SalesTicketVo selectSales(String orderId,String status);

}
