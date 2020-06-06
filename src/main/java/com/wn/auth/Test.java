package com.wn.auth;

import com.wn.common.Const;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.auth
 * @Author: 廖刚
 * @CreateTime: 2020-05-14 11:42
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void generateToken(){
        try {
            String token = Jwts.builder()
                    .claim("id", "200").claim("username", "admin").claim("role", 1)
                    .setExpiration(DateTime.now().plusMinutes(30).toDate())
                    .signWith(SignatureAlgorithm.RS256, RsaUtils.getPrivateKey(Const.RsaPath.PRIVATE_RSA))
                    .compact();
            System.out.println(token);
        } catch (Exception e) {
            System.out.println("token生成失败");
        }
    }

    @org.junit.Test
    public void parseToken(){
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjIwMCIsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlIjoxLCJleHAiOjE1ODk0Mzg5NTJ9.b8UcXAiED0p4Xq1LFPb64Kt61EwVDpQaRMJiNXtKAq016kaeszdzStCeAYe6cxXO3Wcue_3Xhwz3jrNrw0AKPUHFQucmPnjznDVdSSEw1lhCbbFL_boUzZv0v-K-4xQzAxTWRgc6bXswf8xL9lpH86Lg4KAOoOlMEnszkWkUV0s";
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(RsaUtils.getPublicKey(Const.RsaPath.PUBLIC_RSA)).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            Integer id = Integer.parseInt(body.get("id").toString());
            String username = body.get("username").toString();
            Integer role =  Integer.parseInt(body.get("role").toString());
            System.out.println(id);
            System.out.println(username);
            System.out.println(role);
        } catch (Exception e) {
            System.out.println("token解析失败");
        }
    }

}
