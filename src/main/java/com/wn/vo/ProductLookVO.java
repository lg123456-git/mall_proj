package com.wn.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.vo
 * @Author: 廖刚
 * @CreateTime: 2020-05-04 22:49
 * @Description: 查看商品详情需要显示的
 */
@Data
public class ProductLookVO {

    private String name;
    private String subtitle;
    private String categoryname;
    private BigDecimal price;
    private Integer stock;
    private String mainImage;
    private String detail;


}
