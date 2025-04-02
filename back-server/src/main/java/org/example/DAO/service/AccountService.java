package org.example.DAO.service;

import org.example.DAO.entity.DTO.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.DAO.entity.VO.request.ConfirmResetVO;
import org.example.DAO.entity.VO.request.CreateSubAccountVO;
import org.example.DAO.entity.VO.request.EmailResetVO;
import org.example.DAO.entity.VO.request.ModifyEmailVO;
import org.example.DAO.entity.VO.responst.SubAccountVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
* @author 吾骨封灯
* @since 2025-03-26 19:16:07
*/
public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);
    String registerEmailVerifyCode(String type, String email, String address);
    String registerAccountVerifyCode(Account type, String email, String address);
    String resetEmailAccountPassword(EmailResetVO info);
    String resetConfirm(ConfirmResetVO info);
    boolean changePassword(int id, String oldPassword, String newPassword);
    void createSubAccount(CreateSubAccountVO vo);
    void deleteSubAccount(int uid);
    List<SubAccountVO> listSubAccount();
    String modifyEmail(int id, ModifyEmailVO vo);
}
