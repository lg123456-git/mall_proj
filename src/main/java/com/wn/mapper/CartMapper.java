package com.wn.mapper;

import com.wn.pojo.Cart;
import com.wn.vo.CartProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    //首先判断该用户是否已经买过此商品
    Integer hasProduct(@Param("uId") Integer uId,@Param("productId") Integer productId);

    //已经购买过该商品执行的添加操作
    Integer addExistedProduct(@Param("uId") Integer uId,@Param("productId") Integer productId,@Param("count") Integer count);

    //根据用户id查看购物车
    List<CartProductVO> list(@Param("uId") Integer uId);

    //根据购物车id列表清空购物车中已经提交订单的商品
    Integer deleteCartByCartIdList(List<Integer> list);
}