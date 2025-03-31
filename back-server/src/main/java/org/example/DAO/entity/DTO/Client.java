package org.example.DAO.entity.DTO;


import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.DAO.entity.BaseData;

@Data
@TableName("db_client")
@AllArgsConstructor
public class Client implements BaseData {
    @TableId
    private Integer id;
    private String name;
    private String token;
    private String location;
    private String node;
    private Date registerTime;
}