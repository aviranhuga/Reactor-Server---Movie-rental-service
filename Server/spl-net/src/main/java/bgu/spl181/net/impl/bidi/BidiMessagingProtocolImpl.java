package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;

import java.util.ArrayDeque;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String>{

    private Connections<String> connections;
    private int connectionId;
    private Service service;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connections=connections;
        this.connectionId=connectionId;
        service.start();
    }

    @Override
    public void process(String message) {
        TokenHandler tokenHandler = new TokenHandler(message);
        ArrayDeque<String> tokens = tokenHandler.Tokenize();
        if (tokens!=null) return;
        switch (tokens.pollFirst()) {
            case "REGISTER":
                if (tokenHandler.size()>1)

                service.handleRegister()
                break;
            case " ":
        }



    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
