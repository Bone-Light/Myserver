package org.example.DAO.entity.DTO;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.DAO.entity.BaseData;

@Data
@TableName("db_client_ssh")
public class ClientSsh implements BaseData {
    @TableId
    private Integer id;
    private Integer port;
    private String username;
    private String password;
}