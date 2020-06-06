package com.wn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wn.mapper.ShippingMapper;
import com.wn.pojo.Shipping;
import com.wn.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service.impl
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 16:27
 * @Description:
 */
@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 对用户的收货地址进行分页查询
     * @param uId 用户id
     * @return
     */
    @Override
    public PageInfo<Shipping> list(Integer uId, Integer pageNum, Integer pageSize) {
        //对用户的收货地址进行分页查询得到列表结果
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.list(uId);
        PageInfo<Shipping> shippingPageInfo = new PageInfo<>(shippingList);
        return shippingPageInfo;
    }
}
