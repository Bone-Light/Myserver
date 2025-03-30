package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.DAO.entity.DTO.Client;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.request.ClientDetailsVO;
import org.example.DAO.service.ClientService;
import org.example.utils.Const;
import org.springframework.web.bind.annotation.*;


// TODO  Client Controller 等实现

@RestController
@RequestMapping("/monitor")
public class ClientController {
    @Resource
    ClientService clientService;

    @GetMapping("/register")
    public RestBean<Void> registerClient(@RequestHeader("Authorization") String token) {
        // TODO: _UNDO_ verifyAndRegister
        return clientService.verifyAndRegister(token)?
                RestBean.success() : RestBean.failure(401,"客户端注册失败，请检测Token是否正确");
    }

    @PostMapping("/detail")
    public RestBean<Void> updateClientDetails(@RequestAttribute(Const.ATTR_CLIENT) Client client,
                                              @RequestBody @Valid ClientDetailsVO vo){
        // TODO: _UNDO_ updateClientDetail
        clientService.updateClientDetail(vo, client);
        return RestBean.success();
    }


}
