package com.wn.service;

import com.wn.pojo.User;
import com.wn.vo.UserVO;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service
 * @Author: 廖刚
 * @CreateTime: 2020-04-30 17:24
 * @Description:
 */
public interface IUserService {

    //后台登录
    User login(User user);

    //前台登录
    String userLogin(User user);

    //获取用户信息
    UserVO getUserInfo(Integer id);

    //检查用户名是否注册
    Integer checkValid(String username);

    //前台用户注册
    Integer register(User user);

    //忘记密码时，获取用户问题
    String getQuestion(String username);

    //忘记密码时，检查答案是否正确
    String forgetCheckAnswer(User user);

    //忘记密码时修改密码
    Integer updatePassword(String username,String passwordNew);

    //通过主键获取对象
    User selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKey(User record);

    Integer updateLogPassword(String passwordOld,String passwordNew,Integer id);

}
