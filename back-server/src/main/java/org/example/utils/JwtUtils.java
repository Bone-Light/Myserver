package org.example.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}")
    private String key;

    @Value("${spring.security.jwt.expire}")
    private int expire;

    @Value("${spring.security.jwt.limit.base}")
    private int limit_base;

    @Value("${spring.security.jwt.limit.upgrade}")
    private int limit_upgrade;

    @Value("${spring.security.jwt.limit.frequency}")
    private int limit_frequency;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowUtils flowUtils;

    public String createJWT(UserDetails user, String username, int userId){
        if(this.frequencyCheck(userId)){
            Algorithm algorithm = Algorithm.HMAC256(key);
            Date expire = this.expireTime();
            return JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withClaim("id", userId)
                    .withClaim("name", username)
                    .withClaim("authorities", user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority).toList())
                    .withExpiresAt(expire)
                    .withIssuedAt(new Date())
                    .sign(algorithm);
        } else {
            return null;
        }
    }

    public DecodedJWT resolveToken(String headerToken) {
        String token = this.convertToken(headerToken);
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT jwt = jwtVerifier.verify(token);
            if(this.isInvalidToken(jwt.getId())) return null;
            if(this.isInvalidUser(jwt.getClaim("id").asInt())) return null;
            Map<String, Claim> claims = jwt.getClaims();
            return new Date().after(claims.get("exp").asDate()) ? null : jwt;
        } catch (Exception e){
            return null;
        }
    }

/// /////////////
    public void deleteUser(String uid){
        stringRedisTemplate.opsForValue().set(Const.JWT_BLACK_LIST + uid, "", expire, TimeUnit.HOURS);
    }

    public UserDetails toUser(DecodedJWT decodedJWT){
        Map<String, Claim> claims = decodedJWT.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("******")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    public Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire);
        return calendar.getTime();
    }

    public boolean invalidateJwt(String headerToken){
        String token = this.convertToken(headerToken);
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT jwt = jwtVerifier.verify(token);
            return deleteToken(jwt.getId(), jwt.getExpiresAt());
        } catch (Exception e){
            return false;
        }
    }
/// //////////////////

    private boolean frequencyCheck(int userId){
        String key = Const.JWT_FREQUENCY + userId;
        return flowUtils.limitOnceUpgradeCheck(key, limit_frequency, limit_base, limit_upgrade);
    }

    private String convertToken(String headerToken){
        if(headerToken == null || !headerToken.startsWith("Bearer ")) return null;
        return headerToken.substring(7);
    }

    private boolean deleteToken(String uuid, Date time){
        if(this.isInvalidToken(uuid)) return false;
        Date now = new Date();
        long expire = Math.max(time.getTime() - now.getTime(), 0L);
        stringRedisTemplate.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.HOURS);
        return true;
    }

    private boolean isInvalidUser(int uid){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.JWT_BLACK_LIST + uid));
    }

    private boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.JWT_BLACK_LIST + uuid));
    }

}
