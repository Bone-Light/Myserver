package org.example.DAO.entity.VO.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class SshConnectionVO {
    int id;
    int port;
    @NotNull
    @Length(min = 1)
    String username;
    @NotNull
    @Length(min = 1)
    String password;
}
