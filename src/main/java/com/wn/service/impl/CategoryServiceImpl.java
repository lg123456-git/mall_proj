package com.wn.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wn.common.DatagridResponse;
import com.wn.mapper.CategoryMapper;
import com.wn.pojo.Category;
import com.wn.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service.impl
 * @Author: 廖刚
 * @CreateTime: 2020-05-15 15:44
 * @Description:
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 父品类分页查询
     * @param page
     * @param rows
     * @param name
     * @return
     */
    @Override
    public DatagridResponse queryList(Integer page, Integer rows,String name) {
        //在这里如果name不为null且不为""，拼接一下
        if(name != null && !name.equals("")){
            name = "%" + name + "%";
        }
        //开启分页
        Page<Object> pageInfo = PageHelper.startPage(page, rows);
        List<Category> categories = categoryMapper.queryList(name);
        int total = (int) pageInfo.getTotal();
        return new DatagridResponse(total,categories);
    }


    /**
     * //判断父品类是否已经存在,存在返回true，不存在返回false
     * @param name 父品类名称
     * @return
     */
    @Override
    public boolean isExistName(String name) {
        Integer existName = categoryMapper.isExistName(name);
        if(existName > 0){//已经存在
            return true;
        }
        return false;
    }

    @Override
    public Integer addAndUpdateCategory(Integer id, String name) {
        Integer result;
        Category category = new Category();
        category.setName(name);
        if(id == null){//添加父品类操作
            category.setParentId(0);
            result = categoryMapper.insertSelective(category);
        }else{//修改父品类操作
            category.setId(id);
            result = categoryMapper.updateByPrimaryKeySelective(category);
        }
        return result;
    }


    @Override
    public Integer existChild(Integer id) {
        Integer integer = categoryMapper.existChild(id);
        return integer;
    }


    @Override
    public DatagridResponse queryChildList(Integer page, Integer rows, Integer parentId, String name) {
        if(name != null && !name.equals("")){
            name = "%" + name + "%";
        }
        Page<Object> pageInfo = PageHelper.startPage(page, rows);
        List<Category> categories = categoryMapper.queryChildList(parentId,name);
        int total = (int) pageInfo.getTotal();
        return new DatagridResponse(total,categories);
    }


    @Override
    public Integer addAndUpdateChildCategory(Integer id, Integer parentId, String name) {
        //根据品类id是否为null,id=null--增加品类操作，id！=null--修改品类
        Category category = new Category();
        Integer result = 0;
        category.setParentId(parentId);
        category.setName(name);
        if(id == null){//增加品类操作
            result = categoryMapper.insertSelective(category);
        }else{//修改品类
            category.setId(id);
            result = categoryMapper.updateByPrimaryKeySelective(category);
        }
        return result;
    }

    /**
     * 批量删除品类
     * @param ids
     * @return
     */
    @Override
    public Integer deleteCategoryByIds(Integer[] ids) {
        Integer integer = categoryMapper.deleteSomeCategory(ids);
        return integer;
    }

    /**
     * 根据品类id逻辑删除
     * @param id
     * @return
     */
    @Override
    public Integer logicDeleteCategoryById(Integer id) {
        Integer integer = categoryMapper.logicDeleteCategoryById(id);
        return integer;
    }

    /**
     * 查找所有的父品类,除去逻辑删除的父品类
     * @return
     */
    @Override
    public List<Category> selectAllCategoryParent() {
        List<Category> categories = categoryMapper.selectAllCategoryParent();
        return categories;
    }

    /**
     * 根据父品类id,获取所有的子品类
     * @param parentId 父品类id
     * @return
     */
    @Override
    public List<Category> selectCategoryChildFromP(Integer parentId) {
        List<Category> categories = categoryMapper.selectCategoryChildFromP(parentId);
        return categories;
    }


}
