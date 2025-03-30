package org.example.controller;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.DAO.entity.DTO.Account;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.request.*;
import org.example.DAO.entity.VO.responst.ClientPreviewVO;
import org.example.DAO.entity.VO.responst.ClientSimpleVO;
import org.example.DAO.entity.VO.responst.RuntimeHistoryVO;
import org.example.DAO.entity.VO.responst.SshSettingsVO;
import org.example.DAO.service.AccountService;
import org.example.DAO.service.ClientService;
import org.example.utils.Const;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class MonitorController {
    @Resource
    ClientService clientService;

    @Resource
    AccountService accountService;

    // TODO: _UNDO_ accountAccessClients
    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient(@RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        List<ClientPreviewVO> clients = clientService.listClients();
        if(this.isAdminAccount(userRole)) {
            return RestBean.success(clients);
        } else {
            List<Integer> ids = this.accountAccessClients(userId);
            return RestBean.success(
                clients.stream()
                    .filter(vo -> ids.contains(vo.getId()))
                    .toList()
            );
        }
    }
    // TODO: listSimpleList()
    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList(@RequestAttribute(Const.ATTR_USER_ID) String userRole) {
        if(this.isAdminAccount(userRole)) {
            return RestBean.success(clientService.listSimpleList());
        } else {
            return RestBean.noPermission();
        }
    }

    // TODO: renameClient(vo)
    @PostMapping("rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO vo,
                                       @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                       @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        if(this.permissionCheck(userId,userRole,vo.getId())){
            clientService.renameClient(vo);
            return RestBean.success();
        } else {
            return RestBean.noPermission();
        }
    }
    // TODO: renameNode(vo)
    @PostMapping("node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo,
                                     @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                     @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        if(this.permissionCheck(userId,userRole,vo.getId())){
            clientService.renameNode(vo);
            return RestBean.success();
        } else {
            return RestBean.noPermission();
        }
    }
    // TODO: clientDetails(userId)
    @GetMapping("details")
    public RestBean<ClientDetailsVO> details(int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.permissionCheck(userId,userRole,clientId)){
            return RestBean.success(clientService.clientDetails(userId));
        } else {
            return RestBean.noPermission();
        }
    }
    // TODO: clientRuntimeDetailsHistory(clientId)
    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(int clientId,
                                                            @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                            @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.permissionCheck(userId,userRole,clientId)){
            return RestBean.success(clientService.clientRuntimeDetailsHistory(clientId));
        } else {
            return RestBean.noPermission();
        }
    }
    // TODO: clientRuntimeDetailsNow(clientId)
    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(int clientId,
                                                       @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                       @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        if(this.permissionCheck(userId,userRole,clientId)){
            return RestBean.success(clientService.clientRuntimeDetailsNow(clientId));
        } else {
            return RestBean.noPermission();
        }
    }
    // TODO: registerToken()
    @GetMapping("/register")
    public RestBean<String> registerClient(int clientId,
                                           @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.isAdminAccount(userRole)) {
            return RestBean.success(clientService.registerToken());
        } else {
            return RestBean.noPermission();
        }
    }

    // TODO: deleteClient(clientId)
    @GetMapping("/delete")
    public RestBean<String> deleteClient(int clientId,
                                         @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.isAdminAccount(userRole)) {
            clientService.deleteClient(clientId);
            return RestBean.success();
        } else {
            return RestBean.noPermission();
        }
    }
    // TODO: saveClientSshConnection(vo)
    @PostMapping("/ssh-save")
    public RestBean<Void> saveSshClient(@RequestBody @Valid SshConnectionVO vo,
                                        @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                        @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.permissionCheck(userId, userRole, vo.getId())){
            clientService.saveClientSshConnection(vo);
            return RestBean.success();
        } else {
            return RestBean.noPermission();
        }
    }

    // TODO: sshSettings(clientId)
    @GetMapping("/ssh")
    public RestBean<SshSettingsVO> sshClient(int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.permissionCheck(userId, userRole, clientId)){
            return RestBean.success(clientService.sshSettings(clientId));
        } else {
            return RestBean.noPermission();
        }
    }


    private boolean isAdminAccount(String userRole) {
        userRole = userRole.substring(5);
        return Const.ROLE_ADMIN.equals(userRole);
    }
    private List<Integer> accountAccessClients(int uid) {
        Account account = accountService.getById(uid);
        return account.getClientList();
    }
    private boolean permissionCheck(int uid, String role, int ClientId) {
        if(this.isAdminAccount(role)) return true;
        return this.accountAccessClients(uid).contains(ClientId);
    }
}
