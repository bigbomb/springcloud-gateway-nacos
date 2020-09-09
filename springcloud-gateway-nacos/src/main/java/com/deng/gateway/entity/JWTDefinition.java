package com.deng.gateway.entity;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@RefreshScope
public class JWTDefinition {

    private Logger logger = LoggerFactory.getLogger(JWTDefinition.class);
    /**
     * 加密秘钥
     */
    @Value("${jwt.secret}")
    public String secret;
    /**
     * accessToken有效时间
     */
    @Value("${jwt.accessTokenExpire}")
    public long accessTokenExpire;

    /**
     * refreshToken有效时间
     */
    @Value("${jwt.refreshTokenExpire}")
    public long refreshTokenExpire;

    public String header;


    public String generateAccessToken(String userId) {
        System.out.println("header=" + header + ", expire=" + accessTokenExpire + ", secret=" + secret);
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + accessTokenExpire * 1000);
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(userId).setIssuedAt(nowDate)
                .setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        // 注意: JDK版本高于1.8, 缺少 javax.xml.bind.DatatypeConverter jar包,编译出错
    }

    /**
     * 生成Token签名
     * @param userId 用户ID
     * @return 签名字符串
     * @author geYang
     * @date 2018-05-18 16:35
     */
    public String generateRefreshToken(String userId) {
        System.out.println("header=" + header + ", expire=" + refreshTokenExpire + ", secret=" + secret);
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + refreshTokenExpire * 1000);
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(userId).setIssuedAt(nowDate)
                .setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        // 注意: JDK版本高于1.8, 缺少 javax.xml.bind.DatatypeConverter jar包,编译出错
    }
    /**
     * 获取签名信息
     * @param token
     * @author geYang
     * @date 2018-05-18 16:47
     */
    public  Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 判断Token是否过期
     * @param expiration
     * @return true 过期
     * @author geYang
     * @date 2018-05-18 16:41
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }



}