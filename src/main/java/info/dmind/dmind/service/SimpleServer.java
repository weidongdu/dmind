package info.dmind.dmind.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

@Slf4j
public class SimpleServer extends WebSocketServer {

    private static final String host = "localhost";
    private static final int port = 8091;
    private static SimpleServer simpleServer = null;

    public static SimpleServer getSimpleServer() {
        if (simpleServer == null) {
            simpleServer = new SimpleServer(new InetSocketAddress(host, port));
        }
        return simpleServer;
    }

    private SimpleServer() {
    }

    private SimpleServer(InetSocketAddress address) {
        super(address);
    }

    static {
        getSimpleServer();
//        simpleServer.run();
        simpleServer.start();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("connected to the server!"); //This method sends a message to the new client
        send("new connection: ", handshake.getResourceDescriptor()); //This method sends a message to all clients connected
        log.info("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        log.info("received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        log.info("received ByteBuffer from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        log.info("an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        log.info("websocket server started on {}:{} successfully", host, port);
    }

    public void send(String key, String value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("value", value);

        log.info("send {} {}", key, value);
        simpleServer.broadcast(jsonObject.toJSONString());
    }


    public static void main(String[] args) {
//        String host = "localhost";
//        int port = 8091;

//        WebSocketServer server = new SimpleServer(new InetSocketAddress(host, port));
//        server.run();
    }
}
