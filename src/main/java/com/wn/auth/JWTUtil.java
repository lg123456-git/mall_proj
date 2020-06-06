package com.wn.auth;

import com.wn.common.Const;
import com.wn.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.auth
 * @Author: 廖刚
 * @CreateTime: 2020-05-14 13:26
 * @Description:
 */
public class JWTUtil {


    /**
     * 生成token
     * @param user 登录的用户对象
     * @param time token有效期
     * @param rsaPath 私钥路径
     * @return
     */
    public static String generateToken(User user,Integer time,String rsaPath){
        String token = null;
        try {
            token = Jwts.builder()
                    .claim("id", user.getId()).claim("username", user.getUsername()).claim("role", user.getRole())
                    .setExpiration(DateTime.now().plusMinutes(time).toDate())
                    .signWith(SignatureAlgorithm.RS256, RsaUtils.getPrivateKey(rsaPath))
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(token);
        return token;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static User parseToken(String token){
        User user = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(RsaUtils.getPublicKey(Const.RsaPath.PUBLIC_RSA)).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            Integer id = Integer.parseInt(body.get("id").toString());
            String username = body.get("username").toString();
            Integer role =  Integer.parseInt(body.get("role").toString());
            user = new User(id, username, role);
        } catch (Exception e) {
            System.out.println("token解析失败");
        }
        return user;
    }
}
