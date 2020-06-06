package com.wn.web.reception;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.wn.auth.JWTUtil;
import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.Order;
import com.wn.pojo.User;
import com.wn.service.IOrderService;
import com.wn.vo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web.reception
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 16:45
 * @Description:
 */
@RestController
@RequestMapping("order")
public class OrderController extends ServerResponse{
    @Autowired
    private IOrderService orderService;


    /**
     * 获得商品清单展示
     * @param token
     * @return
     */
    @GetMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct( @CookieValue(value = "token",defaultValue = "--------") String token){
        //获取用户id
        User currrentUser = JWTUtil.parseToken(token);
        //查看用户的订单详情列表
        OrderInfo orderInfo = orderService.getOrderCartProduct(currrentUser.getId());
        return setServerRespnse(Const.Reception.STATUS_SUCCESS,orderInfo);
    }


    /**
     * 创建订单和订单详情
     * @param shippingId 收货地址id
     * @return
     */
    @GetMapping("create.do")
    public ServerResponse create(Integer shippingId,@CookieValue(value = "token",defaultValue = "--------") String token){
        //获取用户id
        User currrentUser = JWTUtil.parseToken(token);
        Order order = orderService.create(currrentUser.getId(), shippingId);
        if(order != null){
            return setServerRespnse(Const.Reception.STATUS_SUCCESS,order);
        }else{
            return responseSuccess(Const.Reception.STATUS_ERROR,"创建订单失败");
        }
    }


    @RequestMapping("pay.do")
    public ServerResponse pay(Long orderNo){
        System.out.println("订单编号:"+orderNo);
        Map<String, Object> map = orderService.pay(orderNo);
        return setServerRespnse(Const.Reception.STATUS_SUCCESS,map);

    }


    /**
     * 支付宝回调函数
     */
    @RequestMapping("alipay_callback.do")
    public String alipayCallback(HttpServletRequest request){
        System.out.println("------回调函数------");
        Map<String,String> params = new HashMap<>();

        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){
                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if(!alipayRSACheckedV2){
                System.out.println("非法请求,验证不通过,再恶意请求我就报警找网警了");
                return "failed";
            }
        } catch (AlipayApiException e) {
            System.out.println("支付宝验证回调异常"+e);
            return "failed";
        }
        System.out.println("-----------");
        System.out.println(params);
        Set<String> keySet = params.keySet();
        keySet.forEach(s ->{
            String val = params.get(s);
            System.out.println(s+":"+val);
        });
        Integer result = orderService.alipayCallback(params);
        if(result > 0){
            return "success";
        }else{
            return "failed";
        }
    }

    /**
     * 查询订单支付状态
     */
    @RequestMapping("query_order_pay_status.do")
    public ServerResponse queryOrderPayStatus(Long orderNo){
        System.out.println("查询支付状态");
        //获取数据库状态码
        Integer integer = orderService.queryOrderPayStatus(orderNo);
        if (integer == 20){//成功支付
            System.out.println("已支付");
            return setServerRespnse(Const.Reception.STATUS_SUCCESS,true);
        }else{
            System.out.println("未支付");
            return responseError(Const.Reception.STATUS_ERROR,"未支付");
        }
    }
}
