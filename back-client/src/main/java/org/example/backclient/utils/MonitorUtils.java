package org.example.backclient.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.backclient.entity.BaseDetail;
import org.example.backclient.entity.ConnectionConfig;
import org.example.backclient.entity.RuntimeDetail;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class MonitorUtils {

    @Lazy
    @Resource
    ConnectionConfig config;

    private final SystemInfo info = new SystemInfo();

    private final Properties properties = System.getProperties();

    public BaseDetail monitorBaseDetail() {
        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hardware = info.getHardware();

        double memory = hardware.getMemory().getTotal() / 1024.0 / 1024 / 1024; // GB
        double diskSize = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / 1024.0 / 1024 / 1024;
        String ip = Objects.requireNonNull(this.findNetWorkInterface(hardware)).getIPv4addr()[0];

        return new BaseDetail()
                .setOsArch(properties.getProperty("os.arch"))
                .setOsName(os.getFamily())
                .setOsVersion(os.getVersionInfo().getVersion())
                .setOsBit(os.getBitness())
                .setCpuName(hardware.getProcessor().getProcessorIdentifier().getName())
                .setCpuCore(hardware.getProcessor().getLogicalProcessorCount())
                .setMemory(memory)
                .setDisk(diskSize)
                .setIp(ip);
    }

    public RuntimeDetail monitorRuntimeDetail() {
        double statisticTime = 0.5;
        try {
            HardwareAbstractionLayer hardware = info.getHardware();
            NetworkIF networkInterface = Objects.requireNonNull(this.findNetWorkInterface(hardware));
            CentralProcessor processor = hardware.getProcessor();

            double upload = networkInterface.getBytesSent();
            double download = networkInterface.getPacketsRecv();
            double read = hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum();
            double write = hardware.getDiskStores().stream().mapToLong(HWDiskStore::getWriteBytes).sum();
            long[] ticks = processor.getSystemCpuLoadTicks();  // 获取CPU时钟周期

            Thread.sleep((long)(statisticTime * 1000));

            // 二次采集数据计算差值
            networkInterface = Objects.requireNonNull(this.findNetWorkInterface(hardware));
            upload = (networkInterface.getBytesSent() - upload) / statisticTime;  // 上传速度（B/s）
            download = (networkInterface.getBytesRecv() - download) / statisticTime;  // 下载速度（B/s）
            read = (hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum() - read) / statisticTime;
            write = (hardware.getDiskStores().stream().mapToLong(HWDiskStore::getWriteBytes).sum() - write) / statisticTime;

            // 内存使用量计算（GB）
            double memory = (hardware.getMemory().getTotal() - hardware.getMemory().getAvailable()) / 1024.0 / 1024 / 1024;
            // 磁盘使用量计算（GB）
            double disk = Arrays.stream(File.listRoots())
                    .mapToLong(file -> file.getTotalSpace() - file.getFreeSpace()).sum() / 1024.0 / 1024 / 1024;

            // 构建并返回运行时数据对象
            return new RuntimeDetail()
                    .setCpuUsage(this.calculateCpuUsage(processor, ticks))  // CPU使用率
                    .setMemoryUsage(memory)  // 内存使用量
                    .setDiskUsage(disk)  // 磁盘使用量
                    .setNetworkUpload(upload / 1024)  // 转换为KB/s
                    .setNetworkDownload(download / 1024)  // 转换为KB/s
                    .setDiskRead(read / 1024 / 1024)  // 转换为MB/s
                    .setDiskWrite(write / 1024 / 1024)  // 转换为MB/s
                    .setTimestamp(new Date().getTime());  // 时间戳
        } catch (Exception e) {
            log.error("读取数据运行异常", e);
        }
        return null;
    }

    public List<String> listNetWorkInterfaceName() {
        return info.getHardware().getNetworkIFs()
                .stream()
                .map(NetworkIF::getName)
                .toList();
    }

    private NetworkIF findNetWorkInterface(HardwareAbstractionLayer hardware) {
        try {
            String target = config.getNetworkInterface();
            List<NetworkIF> ifs = hardware.getNetworkIFs()
                    .stream()
                    .filter(inter -> inter.getName().equals(target))
                    .toList();
            if (!ifs.isEmpty()) {
                return ifs.getFirst();
            } else {
                throw new IOException("网卡信息错误，找不到网卡" + target);
            }
        } catch (Exception e) {
            log.error("读取网络接口信息时出错", e);
        }
        return null;
    }

    private double calculateCpuUsage(CentralProcessor processor, long[] prevTicks) {
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cUser = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];

        //Sum
        long totalCpu = cUser + nice + irq + softIrq + steal + cSys + ioWait + idle;
        return (cSys + cUser) * 1.0 / totalCpu;
    }
}
