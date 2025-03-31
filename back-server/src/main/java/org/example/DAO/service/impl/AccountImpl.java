package org.example.DAO.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.DAO.entity.DTO.Account;
import org.example.DAO.entity.VO.request.ConfirmResetVO;
import org.example.DAO.entity.VO.request.CreateSubAccountVO;
import org.example.DAO.entity.VO.request.EmailResetVO;
import org.example.DAO.entity.VO.request.ModifyEmailVO;
import org.example.DAO.entity.VO.responst.SubAccountVO;
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
import java.util.List;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
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

    public String registerEmailVerifyCode(String type, String email, String address){
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
    public String registerAccountVerifyCode(Account type, String email, String address) {
        return "";
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO info) {
        String verify = resetConfirm(new ConfirmResetVO(info.getEmail(), info.getCode()));
        if(verify != null) return verify;
        String email = info.getEmail();
        String password = passwordEncoder.encode(info.getPassword());
        boolean update = this.update().eq("email", email).set("password", password).update();
        if(update){
            this.deleteEmailVerifyCode(email);
        }
        return update ? null : "更新失败, 请联系管理员";
    }

    @Override
    public String resetConfirm(ConfirmResetVO info) {
        String email = info.getEmail();
        String code = this.getEmailVerifyCode(email);
        if(code == null) return "请先获取验证码";
        if(!code.equals(info.getCode())) return "验证码错误,请重新输入";
        return null;
    }

    @Override
    public boolean changePassword(int id, String oldPassword, String newPassword) {
        Account account = this.getById(id);
        String password = account.getPassword();
        if(!passwordEncoder.matches(oldPassword, password)) return false;
        this.update(Wrappers.<Account>update().eq("id", id).set("password", password));
        return true;
    }

    @Override
    public void createSubAccount(CreateSubAccountVO vo) {

    }

    @Override
    public void deleteSubAccount(int uid) {
        this.removeById(uid);
    }

    @Override
    public List<SubAccountVO> listSubAccount() {
        return this.list(Wrappers.<Account>query().eq("role", Const.ROLE_NORMAL))
                .stream().map(account -> {
                    SubAccountVO vo = account.asViewObject(SubAccountVO.class);
                    vo.setClientList(JSONArray.parse(account.getClients()));
                    return vo;
                }).toList();
    }

    @Override
    public String modifyEmail(int id, ModifyEmailVO vo) {
        String code = getEmailVerifyCode(vo.getEmail());
        if(code == null) return "请先获取验证码";
        if(!code.equals(vo.getCode())) return "验证码错误,请重新输入";
        this.deleteEmailVerifyCode(vo.getEmail());
        Account account = this.findAccountByNameOrEmail(vo.getEmail());
        if(account != null && account.getId() != id){return "操作失败,该邮箱已经被其他注册";}
        this.update()
                .set("email", vo.getEmail())
                .eq("id", id)
                .update();
        return null;
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




