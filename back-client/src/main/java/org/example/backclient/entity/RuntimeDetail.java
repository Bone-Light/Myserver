package org.example.backclient.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuntimeDetail {
    long timestamp;
    double cpuUsage;
    double memoryUsage;
    double diskUsage;
    double networkUpload;
    double networkDownload;
    double diskRead;
    double diskWrite;

}
