package com.wn.mapper;

import com.wn.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    //查询品类列表
    List<Category> queryList(@Param("name") String name);

    //判断父品类是否已经存在,存在返回1，不存在返回0
    Integer isExistName(@Param("name") String name);

    //判断该父品类是否有子类
    Integer existChild(@Param("parent_id") Integer id);

    //根据父品类id分页查询它的子类列表
    List<Category> queryChildList(@Param("parentId") Integer parentId,@Param("name") String name);


    //批量删除品类
    Integer deleteSomeCategory(@Param("array") Integer[] array);

    //根据品类id逻辑删除品类
    Integer logicDeleteCategoryById(@Param("id") Integer id);

    //查找所有的父品类,除去逻辑删除的父品类
    List<Category> selectAllCategoryParent();

    //根据父品类id,获取所有的子品类
    List<Category> selectCategoryChildFromP(@Param("parentId") Integer parentId);

    //根据父品类id，获取它的所以的子品类id
    List<Integer> getChildIdByParentId(@Param("parentId") Integer parentId);
}