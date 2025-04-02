package org.example.DAO.entity.DTO;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.DAO.entity.BaseData;

@Data
@TableName("db_account")
@AllArgsConstructor
@NoArgsConstructor
public class Account implements BaseData {
    @TableId(type = IdType.AUTO)
    Integer id;
    String username;
    String email;
    String password;
    String role;
    Date registerTime;
    String clients;

    public List<Integer> getClientList() {
        if(clients == null) return Collections.emptyList();
        return JSONArray.parse(clients).toList(Integer.class);
    }
}