package com.wn.service;

import com.wn.vo.CarVO;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 13:04
 * @Description:
 */
public interface IcartService {

    //添加商品
    Integer add(Integer uId,Integer productId, Integer count);

    //根据用户id查看购物车
    CarVO list(Integer uId);
}
