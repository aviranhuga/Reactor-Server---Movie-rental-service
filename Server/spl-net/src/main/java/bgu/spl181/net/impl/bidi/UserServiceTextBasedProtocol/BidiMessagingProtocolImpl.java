package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands.*;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String>{

    private Connections<String> connections;
    private int connectionId;
    private String username;
    private Boolean logedIn;
    private Service service;
    private ConcurrentHashMap<String,String> onlineUsers;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connections=connections;
        this.connectionId=connectionId;
        service.start();
        logedIn=false;
        username=null;
    }

    public BidiMessagingProtocolImpl(ConcurrentHashMap<String,String> onlineUsers,Service service){
        this.onlineUsers=onlineUsers;
        this.service=service;
    }

    @Override
    public void process(String message) {
        TokenHandler th = new TokenHandler(message);
        th.Tokenize();
        Result result = null;
        //no command , wrong syntax
        if(th.getCommandname()==null)return;

        switch (th.getCommandname()) {
            case "REGISTER":
                result = (new RegisterCommand(th.getName(), th.getPassword(), th.getDataBlock(), service, logedIn)).handle();
                break;
            case "LOGIN":
                result = (new LoginCommand(th.getName(), th.getPassword(), service, onlineUsers, logedIn)).handle();
                if(result.getType().equals("ACK")){
                    logedIn=true;
                    username=th.getName();
                    onlineUsers.put(th.getName(),String.valueOf(connectionId));
                }
                break;
            case "SIGNOUT":
                result = (new SignoutCommand(logedIn)).handle();
                if(result.getType().equals("ACK")){
                    logedIn=false;
                    onlineUsers.remove(username);
                    connections.disconnect(connectionId);
                }
                break;
            case "REQUEST":
                result = (new RequestCommand(this.username,th.getName(),th.getDataBlock(),logedIn,service)).handle();
                if(result.hasBroadcast())
                    broadcast(result.getBroadcast());
                break;
        }
        if(result!=null)connections.send(connectionId,result.getMessage());

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    private void broadcast(String msg){
        Iterator<String> it = onlineUsers.keySet().iterator();
        while(it.hasNext())
            connections.send(Integer.parseInt(onlineUsers.get(it.next())),msg);
    }

}
