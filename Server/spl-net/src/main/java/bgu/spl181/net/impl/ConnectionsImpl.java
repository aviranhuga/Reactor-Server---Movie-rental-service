package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<String,ConnectionHandler<T>> clients = new ConcurrentHashMap<>();

    @Override
    public boolean send(int connectionId, T msg) {
        //Check if the client exists
        if(clients.contains(String.valueOf(connectionId))){
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
        if (clients.contains(String.valueOf(connectionId)))
            clients.remove(String.valueOf(connectionId));
    }

    public void connect(int connectionId, ConnectionHandler<T> ConnectionHandler) {
        if (!clients.contains(String.valueOf(connectionId)))
            clients.put(String.valueOf(connectionId),ConnectionHandler);
    }

}
