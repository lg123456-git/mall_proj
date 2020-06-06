package com.wn.web.back;


import com.wn.auth.CookieUtil;
import com.wn.auth.JWTUtil;
import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.User;
import com.wn.service.IUserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web
 * @Author: 廖刚
 * @CreateTime: 2020-04-30 17:10
 * @Description: 后台用户表现层
 */
@RestController
@RequestMapping("manager/user")
public class ManagerUserController extends ServerResponse{

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @RequestMapping("login")
    public ServerResponse login(User user,HttpServletResponse response){
//        Map<String, Object> map = new HashMap<>();
//        System.out.println(user);
        //对登录密码进行md5加密
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),"wn",5);
        user.setPassword(md5Hash.toString());
        User currentUser = userService.login(user);
        if(currentUser == null){//登陆失败
            return setResultStatus(Const.Manager.LOGIN_STATUS_ERROR);
        }
        if(currentUser.getRole() != 0){//角色错误
            return setResultStatus(Const.Manager.ROLE_LOGIN_STATUS_ERROR);
        }
        //登录成功
        //1.生成token
        String token = JWTUtil.generateToken(currentUser, 3600, Const.RsaPath.PRIVATE_RSA);
        //2.将token添加到浏览器
        CookieUtil.responseCookie("token",token,3600,response);
        //3.这时将token存入redis中
        redisTemplate.opsForValue().set(currentUser.getId().toString(),token);
        return setResultStatus(Const.Manager.LOGIN_STATUS_SUCCESS);
    }

    /**
     * 获取管理员信息
     * @return
     */
    @PostMapping("get_user_info")
    public ServerResponse getUserInfo(@CookieValue("token") String token){
        User currentUser = JWTUtil.parseToken(token);
        //解析成功，将用户名传递给前端
        return setServerRespnse(Const.Manager.LOGIN_STATUS_SUCCESS,currentUser.getUsername());
    }


    /**
     * 退出登陆
     * @return
     */
    @RequestMapping("logout")
    public ServerResponse logOut(@CookieValue("token") String token, HttpServletResponse response){
        //修改cookie中token的值
        CookieUtil.responseCookie("token","xxx",1800,response);
        return setResultStatus(Const.Manager.LOGIN_STATUS_SUCCESS);
    }

}
