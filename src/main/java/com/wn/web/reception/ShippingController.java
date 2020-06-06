package com.wn.web.reception;

import com.github.pagehelper.PageInfo;
import com.wn.auth.JWTUtil;
import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.Shipping;
import com.wn.pojo.User;
import com.wn.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web.reception
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 16:11
 * @Description:
 */
@RestController
@RequestMapping("shipping")
public class ShippingController extends ServerResponse {

    @Autowired
    private IShippingService shippingService;

    @GetMapping("list.do")
    public ServerResponse list(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                               @CookieValue(value = "token",defaultValue = "--------") String token){
        //获取用户id
        User currrentUser = JWTUtil.parseToken(token);
        //对用户的收货地址进行分页查询
        PageInfo<Shipping> shippingPageInfo = shippingService.list(currrentUser.getId(), pageNum, pageSize);

        return setServerRespnse(Const.Reception.STATUS_SUCCESS,shippingPageInfo);
    }
}
