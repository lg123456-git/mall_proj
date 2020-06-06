package com.wn.mapper;

import com.wn.pojo.Orderitem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderitemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orderitem record);

    int insertSelective(Orderitem record);

    Orderitem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orderitem record);

    int updateByPrimaryKey(Orderitem record);

    //创建订单详情
    Integer createOrderItem(@Param("list") List<Orderitem> list);
}