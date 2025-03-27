package org.example.DAO.entity.VO.responst;

import com.alibaba.fastjson2.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class RuntimeHistoryVO {
    double disk;
    double memory;
    List<JSONObject> list = new LinkedList<>();
}
