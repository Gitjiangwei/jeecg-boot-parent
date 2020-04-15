package org.kunze.diansh.entity;

import lombok.Data;
import org.jeecg.common.util.security.entity.SecurityReq;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@Document(indexName = "item",type = "docs",shards = 1,replicas = 0)
public class Item implements Serializable {

    @Id
    private String id;

    /**标题*/
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    /**分类*/
    @Field(type = FieldType.Keyword)
    private String category;

    /**品牌*/
    @Field(type = FieldType.Keyword)
    private String brand;

    /**价格*/
    @Field(type = FieldType.Double)
    private Double price;

    /**图片地址*/
    @Field(index = false,type = FieldType.Keyword)
    private String images;
}
