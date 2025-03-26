package org.example.DAO.service;

import org.example.DAO.entity.DTO.Client;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吾骨封灯
* @since 2025-03-26 19:18:11
*/
public interface ClientService extends IService<Client> {
    String registerToken();
    Client findClientById(int id);
    Client findClientByToken(String token);
    boolean verifyAndRegister(String token);
    void updateClientDetail(ClientDetailVO vo, Client client);
    void updateRuntimeDetail(RuntimeDetailVO vo, Client client);
    List<ClientPreviewVO> listClients();
    List<ClientSimpleVO> listSimpleList();
    void renameClient(RenameClientVO vo);
    void renameNode(RenameNodeVO vo);
    ClientDetailsVO clientDetails(int clientId);
    RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId);
    RuntimeDetailVO clientRuntimeDetailsNow(int clientId);
    void deleteClient(int clientId);
    void saveClientSshConnection(SshConnectionVO vo);
    SshSettingsVO sshSettings(int clientId);
}
