package com.wn.service.impl;


import com.wn.mapper.UserMapper;
import com.wn.pojo.User;
import com.wn.service.IUserService;
import com.wn.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.service.impl
 * @Author: 廖刚
 * @CreateTime: 2020-04-30 17:25
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        User currentUser = userMapper.login(user);

        return currentUser;
    }


    /**
     * 前台用户登录
     * @return
     */
    public String userLogin(User user){
        UserVO currentUser = userMapper.userLogin(user);
        String token = "1";
        if(currentUser != null){
            try {
//                System.out.println(currentUser);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public UserVO getUserInfo(Integer id) {
        UserVO userVO = userMapper.getUserInfo(id);
        return userVO;
    }

    @Override
    public Integer checkValid(String username) {
        Integer integer = userMapper.checkValid(username);
        return integer;
    }

    @Override
    public Integer register(User user) {
        Integer register = userMapper.register(user);

        return register;
    }

    @Override
    public String getQuestion(String username) {
        String question = userMapper.getQuestion(username);
        return question;
    }

    @Override
    public String forgetCheckAnswer(User user) {
        //currentUser不可能为null，由于前面有过判断
        User currentUser = userMapper.getUserByUsername(user);
        String token = "1";

        System.out.println("user:"+user);
        //判断答案是否匹配
        if(currentUser.getAnswer().equals(user.getAnswer())){
            //说明答案正确,将currentUser的属性存入forgetToken
            System.out.println("测试：" + currentUser);
            try {
                //token = JwtUtils.generateToken(new UserInfo(currentUser.getId().longValue(), currentUser.getUsername()), RsaUtils.getPrivateKey(Const.PRIVATE_RSA), 30);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Integer updatePassword(String username, String passwordNew) {
        Integer integer = userMapper.updatePassword(username, passwordNew);
        return integer;
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public Integer updateByPrimaryKey(User record) {
        int i = userMapper.updateByPrimaryKeySelective(record);
        return i;
    }

    @Override
    public Integer updateLogPassword(String passwordOld, String passwordNew, Integer id) {
        Integer integer = userMapper.updateLogPassword(passwordOld, passwordNew, id);
        return integer;
    }


}
