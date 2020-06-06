package com.wn.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.vo
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 17:59
 * @Description:
 */
@Data
public class OrderInfo {

    private List<OrderItemVO> orderItemVoList;
    private String imageHost;
    private BigDecimal productTotalPrice;
}
