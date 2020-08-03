package org.jeecg.common.util;

public class DistanceUtils {
    private static final double EARTH_RADIUS = 6378137;
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /** *//**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double GetDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        double dis = Math.rint(s/100)/10;//这个结果是你要的千米
        return dis;
    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO 自动生成方法存根
        double distance = GetDistance(121.291995,31.233134,121.211994,31.233124);
        double b = Math.rint(distance/100)/10;//这个结果是你要的千米
        System.out.println(b+"km");
        System.out.println("Distance is:"+distance);
    }
}
