package com.wn.web.reception;


import com.wn.auth.CookieUtil;
import com.wn.auth.JWTUtil;
import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.User;
import com.wn.service.IUserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.web
 * @Author: 廖刚
 * @CreateTime: 2020-04-30 20:35
 * @Description: 普通用户表现层
 */
@RestController
@RequestMapping("user")
public class UserController extends ServerResponse {

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("login.do")
    public ServerResponse login(HttpServletResponse response,User user){

//        System.out.println(user);
        //对登录密码进行md5加密
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),"wn",5);
        user.setPassword(md5Hash.toString());
        //调用登录方法
        User currentUser = userService.login(user);
        //生成token
        if(currentUser != null){//登录成功
            String token = JWTUtil.generateToken(currentUser, 30, Const.RsaPath.PRIVATE_RSA);
            //把token存到浏览器
            CookieUtil.responseCookie("token",token,1800,response);
            //这时将token存入redis中
            ValueOperations<String, String> forString = redisTemplate.opsForValue();
            forString.set(currentUser.getId().toString(),token);
            return setServerRespnse(Const.Reception.STATUS_SUCCESS,currentUser);
        }else{
            return responseError(1,"密码错误");
        }
    }


    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @RequestMapping("get_user_info.do")
    public ServerResponse getUserInfo(@CookieValue(value = "token",defaultValue = "-----") String token){
        User currrentUser = JWTUtil.parseToken(token);
        if(currrentUser == null){//解析失败
            return responseError(Const.Reception.STATUS_ERROR,"用户未登录,无法获取当前用户信息");
        }else{//解析成功
            return setServerRespnse(Const.Reception.STATUS_SUCCESS,currrentUser);
        }
    }

    /**
     * 用户退出登陆
     * @param
     * @return
     */
    @RequestMapping("logout.do")
    public ServerResponse logOut(HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        //修改token
        CookieUtil.responseCookie("token","------",1800,response);
        return responseSuccess(Const.Reception.STATUS_SUCCESS,"退出成功");
    }


    @RequestMapping("check_valid.do")
    public Map<String,Object> checkValid(String type,String str){
        Map<String, Object> map = new HashMap<>();
        Integer integer = userService.checkValid(str);
        if(integer > 0){//已经注册
            map.put("status",1);
            map.put("msg","用户已存在");
        }else{
            map.put("status",0);
            map.put("msg","校验成功");
        }
        return map;
    }


    @RequestMapping("register.do")
    public Map<String,Object> register(User user){
        Map<String, Object> map = new HashMap<>();
//        System.out.println(user);
        //对密码进行加密
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),"wn",5);
        user.setPassword(md5Hash.toString());
        //调用业务层注册方法
        Integer register = userService.register(user);
        if(register > 0){
            map.put("status",0);
            map.put("msg","校验成功");
        }else{
            map.put("status",1);
            map.put("msg","用户已存在");
        }
        return map;
    }

    @RequestMapping("forget_get_question.do")
    public Map<String,Object> forgetGetQusetion(String username){
        Map<String, Object> map = new HashMap<>();
        //根据用户名获取用户的问题
        String question = userService.getQuestion(username);
        if(question != null){
            map.put("status",0);
            map.put("data","你是谁");
        }else{
            map.put("status",1);
            map.put("msg","该用户未设置找回密码问题");
        }
        return map;
    }


    @RequestMapping("forget_check_answer.do")
    public Map<String,Object> forgetCheckAnswer(User user){
        Map<String, Object> map = new HashMap<>();
//        System.out.println(user);
        String token = userService.forgetCheckAnswer(user);
        if(token.equals("1")){
            map.put("status",1);
            map.put("msg","问题答案错误");
        }else{
            map.put("status",0);
            map.put("data",token);
        }
        return map;
    }

    @RequestMapping("forget_reset_password.do")
    public Map<String,Object> forgetResetPassword(String username,String passwordNew,String forgetToken){
        Map<String, Object> map = new HashMap<>();
        map.put("status",1);
        map.put("msg","修改密码操作失效");
        //对forgetToken进行公钥解密
        //UserInfo userInfo = null;
        try {

//            System.out.println("userInfo:"+userInfo);
            //无异常
            //先对密码进行MD5加密
            Md5Hash md5Hash = new Md5Hash(passwordNew,"wn",5);
            String password = md5Hash.toString();
            //调用业务层修改密码方法
            Integer integer = userService.updatePassword(username, password);
            if(integer > 0){
                map.put("status",0);
                map.put("msg","修改密码成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 个人中心
     */
    @RequestMapping("get_information.do")
    public Map<String,Object> getInformation(@CookieValue("utoken") String token){
        Map<String, Object> map = new HashMap<>();
        //对token进行解密
        //解密，获取token中的userInfo
//        try {
//            UserInfo userInfo = JwtUtils.getUserInfo(RsaUtils.getPublicKey(Const.PUBLIC_RSA), token);
//            User user = userService.selectByPrimaryKey(Math.toIntExact(userInfo.getId()));
//            //将密码设为空
//            user.setPassword("");
//            map.put("status", 0);
//            map.put("data",user);
//        } catch (Exception e) {
//            //公钥解密失败
//            map.put("status", 10);
//            map.put("msg","用户未登录,无法获取当前用户信息,status=10,强制登录");
//        }
        return map;
    }


    /**
     * 修改
     */
    @RequestMapping("update_information.do")
    public Map<String,Object> updateInformation(@CookieValue("utoken") String token,User user){
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        map.put("msg","用户未登录");
//        System.out.println(user);
        //对token进行解密
//        try {
//            UserInfo userInfo = JwtUtils.getUserInfo(RsaUtils.getPublicKey(Const.PUBLIC_RSA), token);
//            user.setId(Math.toIntExact(userInfo.getId()));
//            System.out.println(user);
//            //调用业务层根据主键修改用户信息方法
//            int i = userService.updateByPrimaryKey(user);
//            //判断是否修改成功
//            if(i > 0){
//                //需要修改token
//                map.put("status", 0);
//                map.put("msg", "更新个人信息成功");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return map;
    }


    /**
     * 登录状态中修改密码
     */
    @RequestMapping("reset_password.do")
    public Map<String,Object> resetPassword(@CookieValue("utoken") String token,String passwordOld,String passwordNew){
        Map<String, Object> map = new HashMap<>();
        System.out.println("passWordOld:"+passwordOld);
        System.out.println("passwordNew:"+passwordNew);
//        //对新旧密码进行MD5加密
//        Md5Hash md5Hash = new Md5Hash(passwordOld,"wn",5);
//        passwordOld = md5Hash.toString();
//        Md5Hash md5Hash2 = new Md5Hash(passwordNew,"wn",5);
//        passwordNew = md5Hash2.toString();
//        try {
//            UserInfo userInfo = JwtUtils.getUserInfo(RsaUtils.getPublicKey(Const.PUBLIC_RSA), token);
//            //获取用户主键
//            Integer id = Math.toIntExact(userInfo.getId());
//            Integer integer = userService.updateLogPassword(passwordOld, passwordNew, id);
//            if(integer > 0){//修改密码成功
//                map.put("status", 0);
//                map.put("msg","修改密码成功");
//            }else{
//                map.put("status", 1);
//                map.put("msg","旧密码输入错误");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return map;
    }



}
