package com.wn.service;

import com.wn.common.DatagridResponse;
import com.wn.common.ServerResponse;
import com.wn.pojo.Product;
import com.wn.vo.ProductLookVO;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service
 * @Author: 廖刚
 * @CreateTime: 2020-05-04 20:47
 * @Description:
 */
public interface IProductService {

    //分页查询商品管理所有商品
    DatagridResponse queryAll(Integer page, Integer rows);

    //根据id获取商品详情
    ProductLookVO queryById(Integer id);

    //上下架商品
    Integer setSaleSatus(Integer productId,Integer status);

    //根据商品id逻辑删除商品
    Integer logicDeleteProductById(Integer id);

    //添加商品
    Integer addProduct(Product product);


    /*----------------前台--------------------*/

    //按商品名关键字搜索或父类id分页查询
    ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

    //根据商品id获取商品详情
    Product getProductDeatilById(Integer productId);
}
