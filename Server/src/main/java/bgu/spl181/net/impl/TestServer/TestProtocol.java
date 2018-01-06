package bgu.spl181.net.impl.TestServer;


import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;

import java.time.LocalDateTime;

public class TestProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private Connections<String> connections;
    private int connectionId;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId=connectionId;
        this.connections=connections;
    }

    @Override
    public void process(String msg) {
        shouldTerminate = "bye".equals(msg);
        System.out.println("[" + LocalDateTime.now() + "]: " + msg);
        System.out.print(this.connectionId + " ");
        connections.broadcast(msg);

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
