package com.wn.web.back;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.wn.common.Const;
import com.wn.common.DatagridResponse;
import com.wn.common.ServerResponse;
import com.wn.pojo.Category;
import com.wn.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web.back
 * @Author: 廖刚
 * @CreateTime: 2020-05-15 11:58
 * @Description:
 */
@RestController
@RequestMapping("manager/category")
public class ManagerCatogaryController extends ServerResponse {

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping("get_deep_category")
    public DatagridResponse getDeepCategory(Integer page, Integer rows,String name){
        //分页查询
        DatagridResponse datagridResponse = categoryService.queryList(page, rows, name);
        return datagridResponse;
    }

    /**
     * 添加或修改父品类
     * @param id 添加时父品类的id
     * @param name 父品类的名称
     * @return
     */
    @PostMapping("addAndUpdate_category")
    public ServerResponse addAndUpdateCategory(Integer id,String name){
        System.out.println("id:"+id);
        System.out.println("name:"+name);
        //添加或者的时候需要判断品类是否已经存在
        boolean existName = categoryService.isExistName(name);
        if(existName){//添加的品类已经存在
            return setResultStatus(Const.Manager.CATEGORY_REPEATE_STATUS_ERROR);
        }
        //执行添加或者修改操作
        Integer integer = categoryService.addAndUpdateCategory(id, name);
        if(integer > 0){
            return setResultStatus(Const.Manager.CATEGORY_STATUS_SUCCESS);
        }
        return setResultStatus(Const.Manager.CATEGORY_STATUS_ERROR);
    }

    /**
     * 根据id删除父品类
     * @param id 父品类id
     * @return
     */
    @PostMapping("deleteByPrimaryKey_category")
    public ServerResponse deleteByPrimaryKeyCategory(Integer id){
        System.out.println("id :" + id);
        //判断该父品类是否有子类
        Integer existChild = categoryService.existChild(id);
        if(existChild > 0){//存在子类
            return setResultStatus(Const.Manager.CATEGORY_HAS_CHILD_STATUS_ERROR);
        }

        Integer integer = categoryService.logicDeleteCategoryById(id);
        if(integer > 0){//删除成功
            return setResultStatus(Const.Manager.CATEGORY_STATUS_SUCCESS);
        }
        return setResultStatus(Const.Manager.CATEGORY_STATUS_ERROR);
    }


    /**
     * 分页查询子品类列表
     * @param parentId 父品id
     * @return
     */
    @RequestMapping("get_children_category")
    public DatagridResponse getChildrenCategory(Integer page,Integer rows,Integer parentId,String name){
        DatagridResponse datagridResponse = categoryService.queryChildList(page, rows, parentId, name);
        return datagridResponse;
    }


    /**
     * 增加或修改子品类的方法
     * @param id 品类id，根据品类id是否为null,id=null--增加品类操作，id！=null--修改品类
     * @param parentId 父品类id
     * @param name 需要修改成的品类名，不管是修改还是增加都需要品类名在数据库中不存在
     * @return
     */
    @PostMapping("addAndUpdate_child_category")
    public ServerResponse addAndUpdateChildCategory(Integer id,Integer parentId,String name){
        System.out.println("id:"+id);
        System.out.println("parentId:"+parentId);
        System.out.println("name:"+name);
        //先品类名name在数据库中是否存在，存在返回true，不存在返回false
        boolean existName = categoryService.isExistName(name);
        if(existName){//存在
            return setResultStatus(Const.Manager.CATEGORY_REPEATE_STATUS_ERROR);
        }
        //执行修改或者增加操作
        Integer integer = categoryService.addAndUpdateChildCategory(id, parentId, name);
        if(integer > 0){
            return setResultStatus(Const.Manager.CATEGORY_STATUS_SUCCESS);
        }
        return setResultStatus(Const.Manager.CATEGORY_STATUS_ERROR);
    }

    /**
     * 根据主键删除子类，可以删除多条数据,
     * 直接用数组接收前端传递的数据
     * @param ids
     * @return
     */
    @PostMapping("deleteChildSByPrimaryKey_category")
    public ServerResponse deleteChildSByPrimaryKeyCategory(Integer[] ids){
        Integer integer = categoryService.deleteCategoryByIds(ids);
        if(integer > 0){//删除成功
            return setServerRespnse(Const.Manager.CATEGORY_STATUS_SUCCESS,ids.length);
        }
        return setResultStatus(Const.Manager.CATEGORY_STATUS_ERROR);
    }

    /**
     * 查找所有的父品类,除去逻辑删除的父品类
     * @return
     */
    @PostMapping("selectAllCategoryParent")
    public List<Category> selectAllCategoryParent(){
        System.out.println("测试--");
        List<Category> categories = categoryService.selectAllCategoryParent();
        return categories;
    }

    /**
     * 根据父品类id,获取所有的子品类
     */
    @PostMapping("selectCategoryChildFromP")
    public List<Category> selectCategoryChildFromP(Integer parentId){
        List<Category> categories = categoryService.selectCategoryChildFromP(parentId);
        return categories;

    }


}
