package com.wn.service;

import com.wn.pojo.Order;
import com.wn.vo.OrderInfo;

import java.util.Map;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 17:56
 * @Description:
 */
public interface IOrderService {


    //根据用户iD查看用户的订单详情列表
    OrderInfo getOrderCartProduct(Integer uId);

    //穿件订单
    Order create(Integer uId, Integer shippingId);


    //支付生成支付宝二维码
    Map<String, Object> pay(Long orderNo);


    //支付宝回调方法，如果成功，改变支付状态为已支付
    Integer alipayCallback(Map<String,String> map);

    public Integer queryOrderPayStatus(Long orderNo);

}
