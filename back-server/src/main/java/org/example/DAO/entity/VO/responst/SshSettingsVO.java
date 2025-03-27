package org.example.DAO.entity.VO.responst;

import lombok.Data;

@Data
public class SshSettingsVO {
    String ip;
    int port = 22;
    String username;
    String password;
}
