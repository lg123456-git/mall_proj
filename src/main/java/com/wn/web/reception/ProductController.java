package com.wn.web.reception;

import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.Product;
import com.wn.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web.reception
 * @Author: 廖刚
 * @CreateTime: 2020-05-20 15:06
 * @Description:
 */
@RestController
@RequestMapping("product")
public class ProductController extends ServerResponse {

    @Autowired
    private IProductService productService;

    @GetMapping("list.do")
    public ServerResponse list(Integer categoryId,String keyword,
                               @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "orderBy",defaultValue = "price_asc") String orderBy){
        ServerResponse serverResponse = productService.list(categoryId, keyword, pageNum, pageSize, orderBy);
        return serverResponse;
    }


    /**
     * 根据商品id获取商品详情
     * @param productId 商品id
     * @return
     */
    @Cacheable(value = "detail",key = "#productId")
    @GetMapping("detail.do")
    public ServerResponse getProductDeatilById(Integer productId){
        Product product = productService.getProductDeatilById(productId);
        return setServerRespnse(Const.Reception.STATUS_SUCCESS,product);
    }


}
