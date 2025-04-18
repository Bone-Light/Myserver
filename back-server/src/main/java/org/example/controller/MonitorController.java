package org.example.controller;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.DAO.entity.DTO.Account;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.request.*;
import org.example.DAO.entity.VO.responst.*;
import org.example.DAO.service.AccountService;
import org.example.DAO.service.ClientService;
import org.example.utils.Const;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService clientService;

    @Resource
    AccountService accountService;

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

    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList(@RequestAttribute(Const.ATTR_USER_ID) String userRole) {
        if(this.isAdminAccount(userRole)) {
            return RestBean.success(clientService.listSimpleList());
        } else {
            return RestBean.noPermission();
        }
    }

    @PostMapping("/rename")
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
    @PostMapping("/node")
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
    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.permissionCheck(userId,userRole,clientId)){
            return RestBean.success(clientService.clientDetails(clientId));
        } else {
            return RestBean.noPermission();
        }
    }

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

    @GetMapping("/register")
    public RestBean<String> registerClient(@RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if(this.isAdminAccount(userRole)) {
            return RestBean.success(clientService.registerToken());
        } else {
            return RestBean.noPermission();
        }
    }

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
    private boolean permissionCheck(int uid, String role, int clientId) {
        if(this.isAdminAccount(role)) return true;
        return this.accountAccessClients(uid).contains(clientId);
    }
}
