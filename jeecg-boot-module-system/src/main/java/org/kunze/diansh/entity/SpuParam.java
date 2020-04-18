package org.kunze.diansh.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
public class SpuParam implements Serializable {

    /**注明*/
    private String group;

    /**该组的所有规格属性*/
    private SpuParams[] params;
}
