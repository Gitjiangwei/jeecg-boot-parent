package org.kunze.diansh.salesTicket;

import org.kunze.diansh.entity.Commodity;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 对接打小票机
 */
public class SalesTicket implements Printable {

    private ArrayList<Commodity> commodityList;

    private String shopName;

    private String cashier;

    private Font font;

    private String saleNum;

    private String saleSum;

    private String practical;

    private String changes;

    private String orders;


    //构造函数
    public SalesTicket(ArrayList<Commodity> commodityList,String shopName,String cashier,String saleNum,String saleSum,String practical,String changes,String orders){
        this.commodityList = commodityList;
        //超市名称
        this.shopName = shopName;
        // 收银员编号
        this.cashier = cashier;
        //商品总数
        this.saleNum = saleNum;
        //总金额
        this.saleSum = saleSum;
        //实收
        this.practical = practical;
        //找零
        this.changes = changes;
        //订单编号
        this.orders = orders;
    }

    /**
     * @param graphics   Graphic指明打印的图形环境
     * @param pageFormat PageFormat指明打印页格式（页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×
     * 	 *            842点）
     * @param pageIndex  pageIndex指明页号
     * @throws PrinterException thrown when the print job is terminated.
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Commodity commodity = null;
        // 转换成Graphics2D 拿到画笔
        Graphics2D graphics2D = (Graphics2D) graphics;
        //设置打印机颜色为黑色
        graphics2D.setColor(Color.BLACK);

        //打印起点坐标
        double x = pageFormat.getImageableX();
        double y = pageFormat.getImageableY();

        //虚线
        float[] dash1 = { 4.0f };
        // width - 此 BasicStroke 的宽度。此宽度必须大于或等于 0.0f。如果将宽度设置为
        // 0.0f，则将笔划呈现为可用于目标设备和抗锯齿提示设置的最细线条。
        // cap - BasicStroke 端点的装饰
        // join - 应用在路径线段交汇处的装饰
        // miterlimit - 斜接处的剪裁限制。miterlimit 必须大于或等于 1.0f。
        // dash - 表示虚线模式的数组
        // dash_phase - 开始虚线模式的偏移量

        //设置画虚线
        graphics2D.setStroke(new BasicStroke(0.5f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,4.0f,dash1,0.0f));
        //设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
        font = new Font("宋体",Font.PLAIN,11);
        graphics2D.setFont(font);//设置字体
        float height = font.getSize2D();//字体高度
        //标题
        graphics2D.drawString(shopName,(float) x+40,(float) y+10);
        float line = 2 * height;
        font = new Font("宋体",Font.PLAIN,8);
        graphics2D.setFont(font);
        height = font.getSize2D();//字体高度

        //显示收银员
        graphics2D.drawString("收银员："+ cashier,(float) x,(float) y + line);

        line += height;
        //显示订单号
        graphics2D.drawString("订单号："+orders,(float) x,(float) y + line);

        line += height;

        //显示标题
        graphics2D.drawString("名称", (float) x + 20, (float) y + line);
        graphics2D.drawString("单价", (float) x + 60, (float) y + line);
        graphics2D.drawString("数量", (float) x + 85, (float) y + line);
        graphics2D.drawString("总额", (float) x + 115, (float) y + line);

        line += height;
        graphics2D.drawLine((int) x,(int) (y+line),(int) x + 158, (int) (y+line));

        //第4行
        line += height;

        //显示内容
        for(int i = 0; i< commodityList.size(); i++){
            Commodity commodity1 = commodityList.get(i);
            graphics2D.drawString(commodity1.getSpuName(),(float) x,(float) y+line);
            line += height;

            graphics2D.drawString(commodity1.getUnitPrice(),(float) x + 60, (float) y+line);
            graphics2D.drawString(commodity1.getSpuNum(),(float) x + 90,(float) y + line );
            graphics2D.drawString(commodity1.getUnitPriceTotle(),(float) x + 120,(float) y + line );
            line += height;
        }
        line += height;

        graphics2D.drawLine((int) x,(int)(y+line),(int) x+158,(int) (y+line));
        line += height;

        graphics2D.drawString("售出商品数：" + saleNum + "件",(float) x,(float) y + line);
        graphics2D.drawString("合计：" + saleSum +"元",(float)x+80,(float)y + line);
        line += height;
        graphics2D.drawString("实收：" + practical + "元",(float) x,(float) y + line);
        graphics2D.drawString("找零：" + changes + "元",(float) x+80,(float) y + line);
        line += height;
        graphics2D.drawString("时间：" + Calendar.getInstance().getTime().toLocaleString(),(float) x,(float) y + line);

        line += height;
        graphics2D.drawString("欢迎下次再来",(float) x + 20,(float) y + line);

        line += height;
        graphics2D.drawString("钱票请当面点清，离开柜台恕不负责",(float) x, (float) y + line);
        switch (pageIndex){
            case 0:
                return PAGE_EXISTS;
            default:
                return NO_SUCH_PAGE;
        }
    }
}
