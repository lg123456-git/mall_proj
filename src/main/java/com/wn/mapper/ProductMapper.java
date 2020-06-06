package com.wn.mapper;


import com.wn.pojo.Product;
import com.wn.vo.ProductLookVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {


    //分页查询商品管理所有商品
    List<Product> queryAll();

    //根据id获取商品详情
    ProductLookVO queryById(@Param("id") Integer id);

    //上下架商品
    Integer setSaleSatus(@Param("productId") Integer productId,@Param("status") Integer status);

    //根据商品id逻辑删除商品
    Integer logicDeleteProductById(Integer id);

    //添加商品
    Integer addProduct(Product product);



    /*-------------------前台-----------------*/
    //按商品名关键字搜索或父类id分页查询
    List<Product> list(@Param("list") List<Integer> list,@Param("keyword") String keyword,@Param("orderBy") String orderBy);

    //根据商品id获取商品详情
    Product getProductDeatilById(@Param("id") Integer id);
}