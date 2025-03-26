package org.example.DAO.entity.DTO;


import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("db_client")
public class Client {
    @TableId
    private Integer id;
    private String name;
    private String token;
    private String location;
    private String node;
    private LocalDateTime registerTime;
}