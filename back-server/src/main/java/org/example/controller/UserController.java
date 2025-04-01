package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.request.ChangePasswordVO;
import org.example.DAO.entity.VO.request.ModifyEmailVO;
import org.example.DAO.entity.VO.responst.SubAccountVO;
import org.example.DAO.service.AccountService;
import org.example.utils.Const;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    AccountService accountService;

    @PostMapping
    public RestBean<Void> changePassword(@RequestBody @Valid ChangePasswordVO vo,
                                           @RequestAttribute(Const.ATTR_USER_ID) int userId){
        return accountService.changePassword(userId, vo.getOldPassword(), vo.getNewPassword()) ?
                RestBean.success() : RestBean.failure(401, "原密码输入错误");
    }

    @PostMapping("/modify-email")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid ModifyEmailVO vo){
        String result = accountService.modifyEmail(id,vo);
        return result == null ? RestBean.success() : RestBean.failure(401, result);
    }

    // uid 将会被动态赋予 { xxx? uid = x }
    @PostMapping("/sub/delete")
    public RestBean<Void> deleteAccount(int uid,
                                        @RequestAttribute(Const.ATTR_USER_ID) int userId){
        if(uid == userId) return RestBean.failure(401, "参数非法");
        accountService.deleteSubAccount(uid);
        return RestBean.success();
    }

    @GetMapping("/sub/list")
    public RestBean<List<SubAccountVO>> subAccountList() {
        return RestBean.success(accountService.listSubAccount());
    }
}
