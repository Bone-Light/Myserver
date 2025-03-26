package org.example.DAO.entity.DTO;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("db_client_ssh")
public class ClientSsh {
    @TableId
    private Integer id;
    private Integer port;
    private String username;
    private String password;
}