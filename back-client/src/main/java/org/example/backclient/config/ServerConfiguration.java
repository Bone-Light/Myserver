package org.example.backclient.config;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.backclient.entity.ConnectionConfig;
import org.example.backclient.utils.MonitorUtils;
import org.example.backclient.utils.NetUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Configuration
public class ServerConfiguration implements ApplicationRunner {
    @Resource
    NetUtils netUtil;
    @Resource
    MonitorUtils monitorUtils;

    @Bean
    ConnectionConfig connectionConfig() {
        log.info("正在加载服务端连接配置...");
        ConnectionConfig config = this.readConfigurationFromFile();
        if(config == null) config = this.registerToServer();
        return config;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("正在向服务端更新基本系统信息");
        netUtil.updateBaseDetails(monitorUtils.monitorBaseDetail());
    }

    private ConnectionConfig registerToServer() {
        Scanner scanner = new Scanner(System.in);
        String token, address, ifName;
        do {
            log.info("请输入要注册的服务端访问地址，地址类似于 '192.168.0.22:8080' 的写法");
            address = scanner.nextLine();
            log.info("请输入服务端生成的用于注册客户端的Token密钥:");
            token = scanner.nextLine();
            List<String> ifs = monitorUtils.listNetWorkInterfaceName();
            if(ifs.size() > 1) {
                log.info("检测到您的主机有多个网卡设备: {}", ifs);
                do {
                    log.info("请选择你要监控的设备名称:");
                    ifName = scanner.nextLine();
                } while (!ifs.contains(ifName));
            } else {
                ifName = ifs.getFirst();
            }
        } while(!netUtil.registerToServer(address,token));
        ConnectionConfig config = new ConnectionConfig(address,token,ifName);
        this.saveConfigurationToFile(config);
        return config;
    }

    private void saveConfigurationToFile(ConnectionConfig config) {
        File dir = new File("Config");
        if(!dir.exists() && dir.mkdir()){
            log.info("创建用于保存服务端连接信息的目录--完成");
            File file = new File("Config");
            try(FileWriter writer = new FileWriter(file)) {
                writer.write(JSONObject.from(config).toJSONString());
            } catch (IOException e) {
                log.error("保存配置文件时出现问题", e);
            }
            log.info("服务端连接信息保存成功");
        }
    }

    private ConnectionConfig readConfigurationFromFile() {
        File configrationFile = new File("config/server.json");
        if(configrationFile.exists()) {
            try(FileInputStream stream = new FileInputStream(configrationFile)){
                String raw = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                return JSONObject.parseObject(raw).to(ConnectionConfig.class);
            } catch (IOException e) {
                log.error("读取配置文件失败", e);
            }
        }
        return null;
    }
}
