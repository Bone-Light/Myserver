package org.example.backclient.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.backclient.entity.BaseDetail;
import org.example.backclient.entity.ConnectionConfig;
import org.example.backclient.entity.Response;
import org.example.backclient.entity.RuntimeDetail;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class NetUtils{
    private final HttpClient client = HttpClient.newHttpClient();

    @Lazy
    @Resource
    ConnectionConfig connectionConfig;

    public boolean registerToServer(String address, String token) {
        log.info("正在向服务端申请注册, 请稍后");
        Response response = this.doGet("/register", address, token);
        if(response == null) {
            log.error("注册失败：无法连接到服务器");
            return false;
        }
        if(response.success()){
            log.info("客户端注册成功");
        } else {
            log.error("客户端注册失败: {}", response.message());
        }
        return response.success();
    }

    public void updateBaseDetails(BaseDetail detail){
        Response response = this.doPost("/detail", detail);
        if(response.success()){
            log.info("系统的基本信息更新完成");
        } else {
            log.error("系统基本信息更新失败: {}", response.message());
        }
    }

    public void updateRuntimeDetails(RuntimeDetail detail){
        Response response = this.doPost("/runtime", detail);
        if(!response.success()){
            log.warn("更新运行时状态, 接收到服务端的异常响应: {}", response.message());
        }
    }


    private Response doGet(String url, String address, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(new URI(address + "/monitor" + url))
                    .header("Authorization", token)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(response.body(), Response.class);
        } catch (Exception e){
            log.error("向服务端发起请求时出现问题", e);
            return Response.errorResponse(e);
        }
    }
    private Response doPost(String url, Object data){
        try {
            String rawData = JSONObject.from(data).toJSONString();
            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(rawData))
                    .uri(new URI(connectionConfig.getAddress() + "/monitor" + url))
                    .header("Authorization", connectionConfig.getToken())
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(response.body()).to(Response.class);
        } catch (Exception e) {
            log.error("在发起服务端请求时出现问题", e);
            return Response.errorResponse(e);
        }
    }
}
