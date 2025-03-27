package org.example.DAO.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.DAO.entity.DTO.ClientDetail;
import org.example.DAO.mapper.ClientDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 吾骨封灯
* @since 2025-03-26 19:18:11
*/
@Service
public class ClientDetailImpl extends ServiceImpl<ClientDetailMapper, ClientDetail>
    implements IService<ClientDetail> {

}




