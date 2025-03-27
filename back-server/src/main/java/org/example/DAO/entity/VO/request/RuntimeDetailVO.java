package org.example.DAO.entity.VO.request;

import jakarta.validation.constraints.NotNull;

public class RuntimeDetailVO {
    @NotNull
    long timestamp;
    @NotNull
    double cpuUsage;
    @NotNull
    double memoryUsage;
    @NotNull
    double diskUsage;
    @NotNull
    double networkUpload;
    @NotNull
    double networkDownload;
    @NotNull
    double diskRead;
    @NotNull
    double diskWrite;
}
