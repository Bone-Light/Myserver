package org.example.DAO.entity.DTO;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("db_account")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String clients;
    private LocalDateTime registerTime;

    public List<Integer> getClientList() {
        if(clients == null) return Collections.emptyList();
        return JSONArray.parse(clients).toList(Integer.class);
    }
}