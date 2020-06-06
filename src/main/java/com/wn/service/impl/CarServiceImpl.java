package com.wn.service.impl;

import com.wn.common.Const;
import com.wn.mapper.CartMapper;
import com.wn.pojo.Cart;
import com.wn.service.IcartService;
import com.wn.vo.CarVO;
import com.wn.vo.CartProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service.impl
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 13:03
 * @Description:
 */
@Service
public class CarServiceImpl implements IcartService {

    @Autowired
    private CartMapper cartMapper;
    /**
     * 添加商品到购物车
     * @param uId 用户id
     * @param productId 商品id
     * @param count 购买数量
     * @return
     */
    @Override
    public Integer add(Integer uId, Integer productId, Integer count) {
        //首先判断该用户是否已经买过此商品
        Integer integer = cartMapper.hasProduct(uId, productId);
        Integer result = -1;
        System.out.println("integer:"+integer);
        if(integer > 0){//已经购买过，执行update
            result = cartMapper.addExistedProduct(uId, productId, count);
        }else{//未购买，执行insert
            Cart cart = new Cart();
            cart.setUserId(uId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            result = cartMapper.insertSelective(cart);
        }
        return result;
    }

    /**
     * 根据用户id查看购物车
     * @param uId 用户id
     * @return
     */
    @Override
    public CarVO list(Integer uId) {
        List<CartProductVO> cartProductVOList = cartMapper.list(uId);
        CarVO carVO = new CarVO();
        carVO.setAllChecked(true);
        carVO.setCartProductVoList(cartProductVOList);
        //计算购物车所有的商品的总价
        BigDecimal  cartTotalPrice = new BigDecimal("0");
        for(CartProductVO cartProductVO:cartProductVOList){
            cartProductVO.setProductMainImage(Const.Image.IMGGE_HOST+cartProductVO.getProductMainImage());
            cartTotalPrice = cartTotalPrice.add(cartProductVO.getProductTotalPrice());
        }
        carVO.setCartTotalPrice(cartTotalPrice);
        return carVO;
    }
}
