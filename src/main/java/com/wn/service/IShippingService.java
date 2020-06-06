package com.wn.service;

import com.github.pagehelper.PageInfo;
import com.wn.pojo.Shipping;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 16:27
 * @Description:
 */

public interface IShippingService {

    //对用户的收货地址进行分页查询
    PageInfo<Shipping> list(Integer uId, Integer pageNum, Integer pageSize);
}
