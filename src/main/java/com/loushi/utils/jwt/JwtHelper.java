package com.loushi.utils.jwt;

import com.loushi.component.contants.JwtCons;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * jwt工具类
 */
public class JwtHelper {

    /**
     * 获取到HttpServletRequest中的uid
     */
    public static Integer getRequestUid(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute(JwtCons.claims);
        return getClaimsUid(claims);
    }


    /**
     * 获取到Claims中的uid
     */
    public static Integer getClaimsUid(Claims claims) {
        return (Integer) claims.get(JwtCons.token_uid);
    }

    /**
     * 获取到HttpServletRequest中的token
     */
    public static String getRequestToken(HttpServletRequest request) {
        return request.getHeader(JwtCons.Authentication);
    }

    /**
     * 解析jwt
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            return Jwts.parser()
                    .setSigningKey(base64Security)
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 构建jwt
     */
    public static String createJWT(Integer uid, Integer ttlSecond, String base64Security) {

        long nowMillis = System.currentTimeMillis();
        Date exp = new Date(nowMillis + ttlSecond * 1000);

        //添加构成JWT的参数
        return Jwts.builder()
                .claim(JwtCons.token_uid, uid)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, base64Security)
                .setExpiration(exp)
                .compact();
    }


    public static void main(String[] args) {
        System.out.println(createJWT(28, 10, "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY="));
    }

}
