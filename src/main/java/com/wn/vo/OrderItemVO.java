package com.wn.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.vo
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 17:49
 * @Description:
 */
@Data
public class OrderItemVO {

    private Integer id;
    private Long orderNo;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal currentUnitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String createTime;
}
