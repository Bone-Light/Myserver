package org.example.DAO.entity.DTO;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("db_client_detail")
public class ClientDetail {
    @TableId
    private Integer id;
    private String osArch;
    private String osName;
    private String osVersion;
    private Integer osBit;
    private String cpuName;
    private Integer cpuCore;
    private Double memory;
    private Double disk;
    private String ip;
}