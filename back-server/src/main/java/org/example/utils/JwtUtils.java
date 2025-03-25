package org.example.utils;


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}")
    private String key;

    @Value("${spring.secrity.jwt.expire}")
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

    public UserDetails toUser(DecodedJWT decodedJWT){
        Map<String, Claim>  claims = decodedJWT.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("******")
                .authorities(claims.get("authoritises").asArray(String.class))
                .build();
    }

    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    private boolean frequencyCheck(int userId){
        String key = ... + userId;
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
        stringRedisTemplate.opsForValue().set(...);
        return true;
    }

    private boolean isInvalidUser(int uid){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey());
    }

    private boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey());
    }

}
