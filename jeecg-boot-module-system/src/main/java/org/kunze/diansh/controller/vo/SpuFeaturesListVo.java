package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuFeaturesListVo implements Serializable {

    private String id;

    private String title;

    private String status;

    private String shopId;

    private Integer shopType;
}
