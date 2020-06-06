package com.wn.service;

import com.wn.common.DatagridResponse;
import com.wn.pojo.Category;

import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service
 * @Author: 廖刚
 * @CreateTime: 2020-05-15 15:44
 * @Description:
 */
public interface ICategoryService {

    //根据page和rows是进行查询
    DatagridResponse queryList(Integer page, Integer rows, String name);


    //判断品类名是否已经存在,存在返回true，不存在返回false
    boolean isExistName(String name);

    //执行添加父品类操作
    Integer addAndUpdateCategory(Integer id,String name);

    //判断该父品类是否有子类
    Integer existChild(Integer id);

    //根据父品类id分页查询它的子类列表
    DatagridResponse queryChildList(Integer page, Integer rows, Integer parentId, String name);


    //增加或者修改子品类
    Integer addAndUpdateChildCategory(Integer id,Integer parentId,String name);

    //根据主键的字符串数组删除品类
    Integer deleteCategoryByIds(Integer[] ids);

    //根据品类id逻辑删除品类
    Integer logicDeleteCategoryById( Integer id);

    //查找所有的父品类,除去逻辑删除的父品类
    public List<Category> selectAllCategoryParent();

    //根据父品类id,获取所有的子品类
    List<Category> selectCategoryChildFromP(Integer parentId);
}
