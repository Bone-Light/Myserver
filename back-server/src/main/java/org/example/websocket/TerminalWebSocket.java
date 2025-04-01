package org.example.websocket;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.DAO.entity.DTO.ClientDetail;
import org.example.DAO.entity.DTO.ClientSsh;
import org.example.DAO.mapper.ClientDetailMapper;
import org.example.DAO.mapper.ClientSshMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@ServerEndpoint("/terminal/{clientId}")
public class TerminalWebSocket {
    private static ClientDetailMapper clientDetailMapper;
    private static ClientSshMapper clientSshMapper;
    private static final Map<Session, Shell> sessionMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Resource
    public void setDetailMapper(ClientDetailMapper detailMapper) {
        TerminalWebSocket.clientDetailMapper = detailMapper;
    }

    @Resource
    public void setSshMapper(ClientSshMapper sshMapper) {TerminalWebSocket.clientSshMapper = sshMapper;}

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("clientId") String clientId) throws IOException {
        ClientDetail detail = clientDetailMapper.selectById(clientId);
        ClientSsh ssh = clientSshMapper.selectById(clientId);
        if(detail == null || ssh == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "该主机无法被识别"));
            return;
        }
        if(this.createSshConnection(session, ssh, detail.getIp())){
            log.info("主机 {} 的SSH连接已经创建", detail.getIp());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Shell shell = sessionMap.get(session);
        OutputStream outputStream = shell.outputStream;
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        Shell shell = sessionMap.get(session);
        if(shell != null) {
            shell.close();
            sessionMap.remove(session);
            log.info("主机 {} SSH 已经断开", shell.jschSession.getHost());
        }
    }

    public void onError(Session session, Throwable error) throws IOException {
        log.error("用户的WebSocket连接出现问题", error);
        session.close();
    }

    private boolean createSshConnection(Session session, ClientSsh ssh, String ip) throws IOException {
        try {
            JSch jsch = new JSch();
            com.jcraft.jsch.Session jschSession = jsch.getSession(ssh.getUsername(), ip, ssh.getPort());
            jschSession.setPassword(ssh.getPassword());
            jschSession.setConfig("StrictHostKeyChecking", "no");
            jschSession.setTimeout(3000);
            jschSession.connect();
            ChannelShell channel = (ChannelShell) jschSession.openChannel("shell");
            channel.setPtyType("xterm");
            channel.connect(1000);
            sessionMap.put(session, new Shell(session, jschSession, channel));
            return true;
        } catch(JSchException e) {
            String message = e.getMessage();
            if(message.equals("Auth fail")){
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "登录 SSH 失败, 用户名或密码错误"));
                log.error("登录 SSH 失败, 用户名或密码错误");
            } else if (message.contains("Connection refused")) {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "连接被拒绝，可能是没有启动SSH服务，或者是端口未开放"));
                log.error("连接被拒绝，可能是没有启动SSH服务，或者是端口未开放");
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, message));
                log.error("连接 SSH 发生错误", e);
            }
        }
        return false;
    }

    private class Shell {
        private final Session session;
        private final com.jcraft.jsch.Session jschSession;
        private final ChannelShell channelShell;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public Shell(Session session, com.jcraft.jsch.Session jschSession, ChannelShell channelShell) throws IOException {
            this.jschSession = jschSession;
            this.session = session;
            this.channelShell = channelShell;
            this.inputStream = channelShell.getInputStream();
            this.outputStream = channelShell.getOutputStream();
            executorService.submit(this::read);
        }

        private void read() {
            try{
                byte[] buffer = new byte[1024*1024];
                int i;
                while((i = inputStream.read(buffer)) != -1){
                    String text = new String(buffer, 0, i, StandardCharsets.UTF_8);
                    session.getBasicRemote().sendText(text);
                }
            } catch (IOException e) {
                log.error("读取 SSH 输入流时出现问题", e);
            }
        }

        public void close() throws IOException {
            inputStream.close();
            outputStream.close();
            channelShell.disconnect();
            jschSession.disconnect();
            executorService.shutdown();
        }
    }
}
