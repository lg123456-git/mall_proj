package com.wn.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 需要传递给前端的购物车信息
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.vo
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 14:57
 * @Description:
 */
@Data
public class CarVO {
    private List<CartProductVO> cartProductVoList;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;
}
