package org.example.DAO.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.DAO.entity.DTO.Account;
import org.example.DAO.service.AccountService;
import org.example.DAO.mapper.AccountMapper;
import org.example.utils.Const;
import org.example.utils.FlowUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
* @author 吾骨封灯
* @since 2025-03-26 19:16:07
*/
@Service
public class AccountImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{
    @Value("${spring.web.verify.mail-limit}")
    int verifyLimit;
    @Resource
    AmqpTemplate amqpTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    FlowUtils flowUtils;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUesrname(String username){
        Account account = this.findAccountByNameOrEmail(username);
        if(account == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build() ;
    }

    public String registerEmailVerificationCode(String type, String email, String address){
        synchronized (address.intern()) {
            if(!this.verifyLimit(address)) return "请求频繁,稍后再试";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            rabbitTemplate.convertAndSend(Const.MQ_MAIL, data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    @Override
    public Account findAccountByNameOrEmail(String text){
        return this.query()
                .eq("username", text).or()
                .eq("email", text)
                .one();
    }

    private void deleteEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        stringRedisTemplate.delete(key);
    }

    private String getEmailVerifyCode(String email){
        String key = Const.VERIFY_EMAIL_DATA +  email;
        return stringRedisTemplate.opsForValue().get(key);
    }

    private boolean verifyLimit(String address){
        String key = Const.VERIFY_EMAIL_LIMIT + address;
        return flowUtils.limitOnceCheck(key, verifyLimit);
    }
}




