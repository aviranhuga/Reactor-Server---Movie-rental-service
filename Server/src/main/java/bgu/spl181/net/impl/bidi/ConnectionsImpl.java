package bgu.spl181.net.impl.bidi;


import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.util.HashMap;
import java.util.Iterator;

public class ConnectionsImpl<T> implements Connections<T> {

    private HashMap<String, ConnectionHandler<T>> clients = new HashMap<>();
    private int connectionId = 0;

    @Override
    public boolean send(int connectionId, T msg) {
        //Check if the client exists
        if(clients.containsKey(String.valueOf(connectionId))){
            clients.get(String.valueOf(connectionId)).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        Iterator<String> it = clients.keySet().iterator();
        while(it.hasNext())
            clients.get(it.next()).send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        if (clients.containsKey(String.valueOf(connectionId)))
            clients.remove(String.valueOf(connectionId));
    }

    public int connect(ConnectionHandler<T> ConnectionHandler) {
            clients.put(String.valueOf(++connectionId),ConnectionHandler);
            return connectionId;
    }

}
