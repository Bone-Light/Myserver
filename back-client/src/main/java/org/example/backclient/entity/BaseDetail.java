package org.example.backclient.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseDetail {
    String osArch;
    String osName;
    String osVersion;
    int osBit;
    String cpuName;
    int cpuCore;
    double memory;
    double disk;
    String ip;
}
