package com.wn.service.impl;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.wn.common.Const;
import com.wn.mapper.CartMapper;
import com.wn.mapper.OrderMapper;
import com.wn.mapper.OrderitemMapper;
import com.wn.pojo.Order;
import com.wn.pojo.Orderitem;
import com.wn.service.IOrderService;
import com.wn.vo.OrderInfo;
import com.wn.vo.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service.impl
 * @Author: 廖刚
 * @CreateTime: 2020-05-21 17:58
 * @Description:
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderitemMapper orderitemMapper;

    @Autowired
    private CartMapper cartMapper;
    /**
     * 获得商品清单展示
     * @param uId 用户id
     * @return
     */
    @Override
    public OrderInfo getOrderCartProduct(Integer uId) {
        List<OrderItemVO> orderItemVOList = orderMapper.getOrderCartProduct(uId);
        BigDecimal productTotalPrice = new BigDecimal("0");
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setImageHost(Const.Image.IMGGE_HOST);
        orderInfo.setOrderItemVoList(orderItemVOList);
        for (OrderItemVO orderItemVO:orderItemVOList){
            productTotalPrice = productTotalPrice.add(orderItemVO.getTotalPrice());
            //把图片的服务器地址加上
            orderItemVO.setProductImage(Const.Image.IMGGE_HOST+orderItemVO.getProductImage());
        }
        orderInfo.setProductTotalPrice(productTotalPrice);
        return orderInfo;
    }

    /**
     *创建订单和订单详情
     * @param uId 用户id
     * @param shippingId 收货地址id
     * @return
     */
    @Override
    public Order create(Integer uId, Integer shippingId) {
        //存放需要添加的订单详情
        Integer result = -1;
        List<Orderitem> list = new ArrayList<>();
        //存放需要删除的购物车id
        List<Integer> cartIdList = new ArrayList<>();
        //生成订单编号
        long orderNo = (System.currentTimeMillis() + (long) (Math.random()*100));
        //1.查询商品清单
        //获取商品需支付价格
        BigDecimal payment = new BigDecimal("0");
        List<OrderItemVO> orderItemVOList = orderMapper.getOrderCartProduct(uId);
        for (OrderItemVO orderItemVO:orderItemVOList){
            Orderitem orderitem = new Orderitem();
            orderitem.setUserId(uId);
            orderitem.setOrderNo(orderNo);
            orderitem.setProductId(orderItemVO.getProductId());
            orderitem.setProductName(orderItemVO.getProductName());
            orderitem.setProductImage(orderItemVO.getProductImage());
            orderitem.setCurrentUnitPrice(orderItemVO.getCurrentUnitPrice());
            orderitem.setQuantity(orderItemVO.getQuantity());
            orderitem.setTotalPrice(orderItemVO.getTotalPrice());
            list.add(orderitem);
            cartIdList.add(orderItemVO.getId());
            payment = payment.add(orderItemVO.getTotalPrice());

        }
        //2.创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uId);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(1);
        order.setPostage(0);
        order.setStatus(10);
        result  = orderMapper.insertSelective(order);
        //3.创建订单详情
        result = orderitemMapper.createOrderItem(list);
        //4.根据购物车id列表清空购物车中已经提交订单的商品
        result = cartMapper.deleteCartByCartIdList(cartIdList);

        return order;

    }

    /**
     * 生成支付宝二维码，
     * @param orderNo 订单编号
     * @return map集合有二维码地址
     */
    public Map<String, Object> pay(Long orderNo) {
        Map<String, Object> map = new HashMap<>();
        Long id = Long.valueOf(11111);

        //需要从数据库中查询订单编号outTradeNo,totalAmount
        //根据订单编号id这里就是order_no查询应付价格
        BigDecimal payment = orderMapper.getPayment(orderNo);

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
//        String outTradeNo = "tradeprecreate" + System.currentTimeMillis()
//                + (long) (Math.random() * 10000000L);
        String outTradeNo = orderNo.toString();
        map.put("orderNo",outTradeNo);
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "xxx品牌xxx门店当面付扫码消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = payment.toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品3件共20.00元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl("http://2y5xs9.natappfree.cc/MallProj/order/alipay_callback.do")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeServiceImpl tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                //log.info("支付宝预下单成功: )");
                System.out.println("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response = result.getResponse();
                //dumpResponse(response);

                // 需要修改为运行机器上的路径
               // String filePath = String.format("C:/qr-%s.png",
                //        response.getOutTradeNo());
               // System.out.println(filePath);
                //log.info("filePath:" + filePath);
                //System.out.println("filePath:" + filePath);
                //二维码网址
                System.out.println(response.getQrCode());
                //将二维码地址存入map中
                map.put("qrPath",response.getQrCode());
                //生成二维码到本地
                //ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                break;

            case FAILED:
                System.out.println("支付宝预下单失败!!!");
                //log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                //log.error("系统异常，预下单状态未知!!!");
                System.out.println("系统异常，预下单状态未知!!!");
                break;

            default:
                //log.error("不支持的交易状态，交易返回异常!!!");
                System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
        //获取订单编号和二维码路径
        return map;
    }

    /**
     * 支付宝回调方法
     * @param map 支付宝返回的参数
     * @return
     */
    @Override
    public Integer alipayCallback(Map<String, String> map) {
        Integer result = -1;
        if(map!=null && map.get("trade_status").equals("TRADE_SUCCESS")){//支付成功
            //调用持久化层方法，将数据库中支付状态修改成已支付
            Long orderNo = Long.parseLong(map.get("out_trade_no"));
            result = orderMapper.setStatus(orderNo);
        }
        return result;
    }

    /**
     * 查询订单支付状态
     * @param orderNo 订单编号
     * @return 订单状态码
     */
    @Override
    public Integer queryOrderPayStatus(Long orderNo) {
        Integer integer = orderMapper.queryOrderPayStatus(orderNo);
        return integer;
    }


}
