package com.wn.mapper;

import com.wn.pojo.Order;
import com.wn.vo.OrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    //根据用户iD查看用户的订单详情列表
    List<OrderItemVO> getOrderCartProduct(@Param("uId") Integer uId);

    //根据订单编号order_no查询应付价格
    BigDecimal getPayment(@Param("orderNo") Long orderNo);

    //修改支付状态为已支付
    Integer setStatus(@Param("orderNo") Long orderNo);

    //查询订单支付状态
    Integer queryOrderPayStatus(@Param("orderNo") Long orderNo);

}