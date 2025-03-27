package org.example.backclient.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.backclient.entity.ConnectionConfig;
import org.example.backclient.entity.Response;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;

@Slf4j
@Component
public class NetUtils{
    private final HttpClient client = HttpClient.newHttpClient();

    @Lazy
    @Resource
    ConnectionConfig connectionConfig;

    public boolean registerToServer(String address, String token) {
        log.info("正在向服务端申请注册, 请稍后");
        Response response = this.;
    }


    private Response doGet(String url, String address, String token) {}
    private Response doPost(String url, Object data){};
}
