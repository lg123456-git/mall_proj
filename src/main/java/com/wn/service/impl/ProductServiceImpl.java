package com.wn.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wn.common.Const;
import com.wn.common.DatagridResponse;
import com.wn.common.ServerResponse;
import com.wn.mapper.CategoryMapper;
import com.wn.mapper.ProductMapper;
import com.wn.pojo.Product;
import com.wn.service.IProductService;
import com.wn.vo.ProductLookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service.impl
 * @Author: 廖刚
 * @CreateTime: 2020-05-04 20:48
 * @Description:
 */
@Service
public class ProductServiceImpl extends ServerResponse implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public DatagridResponse queryAll(Integer page, Integer rows) {
        Page<Object> pageInfo = PageHelper.startPage(page, rows);
        List<Product> productVOList = productMapper.queryAll();
        int total = (int) pageInfo.getTotal();
        return new DatagridResponse(total,productVOList);
    }

    @Override
    public ProductLookVO queryById(Integer id) {
        ProductLookVO productLookVO = productMapper.queryById(id);
        System.out.println(productLookVO);
        return productLookVO;
    }

    /**
     * 商品上下架操作
     * @param productId
     * @param status
     * @return
     */
    @Override
    public Integer setSaleSatus(Integer productId, Integer status) {
        Integer integer = productMapper.setSaleSatus(productId, status);
        return integer;
    }

    /**
     * 根据商品id逻辑删除商品
     * @param id 商品id
     * @return
     */
    @Override
    public Integer logicDeleteProductById(Integer id) {
        Integer integer = productMapper.logicDeleteProductById(id);
        return integer;
    }

    /**
     * 添加商品
     * @param product
     * @return
     */
    @Override
    public Integer addProduct(Product product) {
        Integer integer = productMapper.addProduct(product);
        return integer;
    }



    /*----------------前台--------------------*/

    //按商品名关键字搜索或父类id分页查询

    @Override
    public ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if(keyword != null){
            keyword = "%" + keyword + "%";
        }
        List<Integer> list = new ArrayList<>();
        if(categoryId != null){//根据品类id查询商品列表
            //1.假设它是父品id,获取父品类获取它的所有子品类id
            list = categoryMapper.getChildIdByParentId(categoryId);
        }
        if(list.size() == 0){//传递的是子品类id
            //将子品类id加入list中再进行查询
            list.add(categoryId);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.list(list, keyword, orderBy);

        productList.forEach( s ->{
            s.setMainImage(Const.Image.IMGGE_HOST+s.getMainImage());
        });
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        return setServerRespnse(Const.Reception.STATUS_SUCCESS,pageInfo);
    }

    /**
     * 根据商品id获取商品详情
     * @param productId 商品id
     * @return
     */
    @Override
    public Product getProductDeatilById(Integer productId) {
        Product product = productMapper.getProductDeatilById(productId);
        product.setImageHost(Const.Image.IMGGE_HOST);
        return product;
    }


}
