package org.example.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.request.ConfirmResetVO;
import org.example.DAO.entity.VO.request.EmailResetVO;
import org.example.DAO.service.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录验证相关", description = "包括用户的登录，注册，验证码等请求操作")
public class AuthorizeController {
    @Resource
    AccountService accountService;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(reset|modify)") String type,
                                        HttpServletRequest request) {
        // TODO:  AccountService:registerEmailVerifyCode 等待实现
        return this.messageHandle(() -> accountService.registerEmailVerifyCode(type, String.valueOf(email), request.getRemoteAddr()));
    }

    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo){
        // TODO:  AccountService:resetConfirm 等待实现
        return this.messageHandle(() -> accountService.resetConfirm(vo));
    }

    @PostMapping("/reset-password")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailResetVO vo){
        // TODO:  AccountService:resetEmailAccountPassword 等待实现
        return this.messageHandle(() ->
                accountService.resetEmailAccountPassword(vo));
    }

    private <T> RestBean<T> messageHandle(Supplier<String> action) {
        String message = action.get();
        if(message == null) {
            return RestBean.success();
        } else {
            return RestBean.failure(400, message);
        }
    }
}
