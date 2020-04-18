package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuParams implements Serializable {

    /**属性名称*/
    private String k;

    /**是否可以搜索*/
    private Boolean searchable;

    /**是否为全局属性*/
    private Boolean global;

    /**是否为数值*/
    private Boolean numerical;

    /**单位,如：克，毫米。如果是数值类型，那么就需要有单位，否则可以不填*/
    private String unit;

    /**属性值的可选项*/
    private String[] options;

    private String v;
}
