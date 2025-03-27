package org.example.DAO.entity.VO.responst;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class SubAccountVO {
    int id;
    String username;
    String email;
    JSONArray clientList;
}
