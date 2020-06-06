package com.wn.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.auth
 * @Author: 廖刚
 * @CreateTime: 2020-05-14 13:37
 * @Description:
 */
public class CookieUtil {

    //向浏览器添加cookie
    public static void responseCookie(String key, String value, Integer time, HttpServletResponse response){
        Cookie cookie = new Cookie(key,value);
        //设置cookie适用的域
        cookie.setDomain("bajin.com");
        //设置cookie的有效期
        cookie.setMaxAge(time);
        //设置可以操作cookie的返回
        cookie.setPath("/");
        //添加cookie到浏览器
        response.addCookie(cookie);
    }
}
