package com.wn.mapper;

import com.wn.pojo.User;
import com.wn.vo.UserVO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    //通过主键获取用户详细信息
    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(User user);

    UserVO userLogin(User user);

    UserVO getUserInfo(@Param("id") Integer id);

    //前天用户注册检查用户名是否唯一
    Integer checkValid(@Param("username") String username);
    //用户注册
    Integer register(User user);

    //根据用户名获取用户问题
    String getQuestion(@Param("username") String username);

    //根据用户名获取用户对象
    User getUserByUsername(User user);

    //修改密码方法
    Integer updatePassword(@Param("username") String username,@Param("password") String passwordNew);

    //登录中修改密码,根据主键和旧密码
    Integer updateLogPassword(@Param("passwordOld") String passwordOld,@Param("passwordNew") String passwordNew,@Param("id") Integer id);



}