package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class Rider implements Serializable {

    //主键id
    private String id;

    /**骑手名称*/
    private String riderName;

    /**联系方式*/
    private String telphone;

    /**身份证号码*/
    private String idenitiy;

    /**接单数*/
    private String orderNumber;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**省*/
    private String province;

    /**市*/
    private String city;

    /**县/区*/
    private String area;
}
