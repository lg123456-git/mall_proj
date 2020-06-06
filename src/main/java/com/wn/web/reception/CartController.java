package com.wn.web.reception;

import com.wn.auth.CookieUtil;
import com.wn.auth.JWTUtil;
import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.User;
import com.wn.service.IcartService;
import com.wn.vo.CarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web.reception
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 11:46
 * @Description:
 */
@RestController
@RequestMapping("cart")
public class CartController extends ServerResponse {

    @Autowired
    private IcartService cartService;

    /**
     * 用户添加购物车
     * @param productId 商品id
     * @param count 商品数量
     * @return
     */
    @GetMapping("add.do")
    public ServerResponse add(@CookieValue(value = "token",defaultValue = "--------") String token, Integer productId, Integer count){
        //解析token,因为有拦截器，所以这里一定可以解析成功,获取用户id
        User currentUser = JWTUtil.parseToken(token);
        Integer integer = cartService.add(currentUser.getId(), productId, count);
        if(integer > 0){//添加成功
            return responseSuccess(Const.Reception.STATUS_SUCCESS,"添加购物车成功");
        }else {
            return responseSuccess(Const.Reception.STATUS_ERROR,"添加购物车失败");
        }
    }


    /**
     * 通过用户id查看用户的购物车
     * @return
     */
    @GetMapping("list.do")
    public ServerResponse list(@CookieValue(value = "token",defaultValue = "--------") String token){
        //解析token,因为有拦截器，所以这里一定可以解析成功,获取用户id
        User currentUser = JWTUtil.parseToken(token);
        System.out.println("userId:"+currentUser.getId());
        CarVO carVO = cartService.list(currentUser.getId());
        return setServerRespnse(Const.Reception.STATUS_SUCCESS,carVO);
    }



}
