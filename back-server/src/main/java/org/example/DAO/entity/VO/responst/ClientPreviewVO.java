package org.example.DAO.entity.VO.responst;

import lombok.Data;

@Data
public class ClientPreviewVO {
    int id;
    boolean online;
    String name;
    String location;
    String osName;
    String osVersion;
    String ip;
    String cpuName;
    int cpuCore;
    double memory;
    double cpuUsage;
    double memoryUsage;
    double networkUsage;
    double networkUpload;
    double networkDownload;
}
