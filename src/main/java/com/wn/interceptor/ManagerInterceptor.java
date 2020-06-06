package com.wn.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wn.auth.CookieUtil;
import com.wn.auth.JWTUtil;
import com.wn.common.Const;
import com.wn.common.ServerResponse;
import com.wn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.interceptor
 * @Author: 廖刚
 * @CreateTime: 2020-05-15 10:27
 * @Description:
 */
@Component
public class ManagerInterceptor extends ServerResponse implements  HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public ManagerInterceptor() {
        System.out.println("构造方法");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //获取cookie
        Cookie[] cookies = request.getCookies();
//        System.out.println(cookies + "-----");
        //判断cookies是否为空
//        System.out.println(cookies);
        if(cookies == null){
            //验证失败，向前端写入json数据失败status
            responseError(response);
            return false;
        }
        //遍历cookies
        String token = null;
        for(Cookie c:cookies){
            if(c.getName().equals("token")){
                token = c.getValue();
            }
        }
//        System.out.println(token);
        //判断是否取到了token
        if(token == null){
            //验证失败，向前端写入json数据失败status
            responseError(response);
            return false;
        }

        //token不会空,对token进行解析
        User user = JWTUtil.parseToken(token);
        if(user == null){//解析失败
            //验证失败，向前端写入json数据失败status
            responseError(response);
            return false;
        }
        //解析成功，还需要判断客户端传过来的token和redis中的token是否一致
        if(!token.equals(redisTemplate.opsForValue().get(user.getId().toString()))){
            //token不一致
            responseError(response);
            CookieUtil.responseCookie("token","------",1800,response);
            return false;
        }
//        System.out.println(user);
        //解析成功，放行
        return true;
    }

    /**
     * 验证失败，向前端写入json数据失败status的方法
     */

    public void responseError(HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(setResultStatus(500));
        PrintWriter writer = response.getWriter();
        writer.write(json);
    }
}
