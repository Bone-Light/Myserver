package org.example.DAO.entity.VO.responst;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    String username;
    String email;
    String role;
    String token;
    Date expires;
}
