package com.wn.web.back;

import com.wn.common.Const;
import com.wn.common.DatagridResponse;
import com.wn.common.ServerResponse;
import com.wn.pojo.Product;
import com.wn.service.IProductService;
import com.wn.util.Base64ToImageUtil;
import com.wn.vo.ProductLookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web
 * @Author: 廖刚
 * @CreateTime: 2020-05-01 15:01
 * @Description: 管理商品表现层
 */
@Controller
@RestController
@RequestMapping("manage/product")
public class ManagerProductController extends ServerResponse {

    @Autowired
    private IProductService productService;

    @RequestMapping("list")
    public DatagridResponse queryAll(Integer page, Integer rows){
        DatagridResponse datagridResponse = productService.queryAll(page, rows);
        return datagridResponse;
    }


    /**
     * 商品详情
     */
    @RequestMapping("lool_procuct")
    public ProductLookVO lookProduct(Integer id){
        Map<String, Object> map = new HashMap<>();
        ProductLookVO productLookVO = productService.queryById(id);
        productLookVO.setMainImage(Const.Image.IMGGE_HOST + productLookVO.getMainImage());
        return productLookVO;
    }

    /**
     * 商品的上下架
     */
    @RequestMapping("set_sale_status")
    public Integer setSaleSatus(Integer productId,Integer status){
        Integer integer = productService.setSaleSatus(productId, status);
        return integer;
    }

    /**
     *逻辑删除商品
     * @param id 商品id
     * @return
     */
    @PostMapping("delete_product")
    public ServerResponse logicDeleteProductById(Integer id){
        Integer integer = productService.logicDeleteProductById(id);
        if(integer > 0){//删除成功
            return setServerRespnse(Const.Manager.CATEGORY_STATUS_SUCCESS,integer);
        }
        return setResultStatus(Const.Manager.CATEGORY_STATUS_ERROR);
    }

    @PostMapping("add_procuct")
    public ServerResponse addProcuct(Product product){
        //1.获取主图
        String mainImage = product.getMainImage();
        //2.字符串截取，拿到base64编码
        int indexOf = mainImage.indexOf("base64");
        String imgBase64Str = mainImage.substring(indexOf+7);
        //3.调用工具类方法,将base64字符串转换成图片,并且存放在本地
        //uuid生成图片名称
        String mainImageName = UUID.randomUUID().toString() + ".png";
        Base64ToImageUtil.generateImage(imgBase64Str,"D:\\data\\MallProj\\fileupload\\"+mainImageName);
        //将商品详情中的图片存放在本地，并且用新的图片地址替换掉原来的字符串
        String detail = product.getDetail();
        String newDetail = Base64ToImageUtil.base64ImgToFile(detail, "D:\\data\\MallProj\\fileupload\\", Const.Image.IMGGE_HOST);
        //调用业务层增加商品方法
        product.setMainImage(mainImageName);
        product.setDetail(newDetail);
        product.setSubImages(mainImageName);
        Integer integer = productService.addProduct(product);
        if(integer > 0){//添加商品成功
            return setResultStatus(Const.Manager.CATEGORY_STATUS_SUCCESS);
        }else{
            return setResultStatus(Const.Manager.CATEGORY_STATUS_ERROR);
        }
    }

}
