package bgu.spl181.net.impl.BBreactor;


import bgu.spl181.net.impl.bidi.BBservice.MovieRentalService;
import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.BidiMessagingProtocolImpl;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.messagingEncoderDecoderImpl;
import bgu.spl181.net.srv.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReactorMain {

    public static void main(String[] args){

        //create the service
        String userpath = "example_Users.json";
        String moviespath ="example_Movies.json";
        ReadWriteLock userslock = new ReentrantReadWriteLock();
        ReadWriteLock movieslock = new ReentrantReadWriteLock();

        //online users
        ConcurrentHashMap<String, String> onlineUsers = new ConcurrentHashMap<>();

        //create server
        Server.reactor(6,
                7777,
                () -> new BidiMessagingProtocolImpl(onlineUsers,new MovieRentalService(userpath,moviespath,userslock,movieslock)),
                () -> new messagingEncoderDecoderImpl()
        ).serve();
    }
}
