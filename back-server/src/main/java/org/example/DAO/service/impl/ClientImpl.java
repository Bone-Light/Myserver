package org.example.DAO.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.DAO.entity.DTO.Client;
import org.example.DAO.service.ClientService;
import org.example.DAO.mapper.ClientMapper;
import org.springframework.stereotype.Service;

/**
* @author 吾骨封灯
* @since 2025-03-26 19:18:11
*/
@Service
public class ClientImpl extends ServiceImpl<ClientMapper, Client>
    implements ClientService{

}




