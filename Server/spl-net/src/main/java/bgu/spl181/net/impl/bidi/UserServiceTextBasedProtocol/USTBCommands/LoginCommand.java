package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands;

import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

import java.util.concurrent.ConcurrentHashMap;


public class LoginCommand extends USTBCommand {

    protected String username;
    protected String password;
    protected ConcurrentHashMap<String,String> onlineUsers;

    public LoginCommand(String username, String password,Service service,ConcurrentHashMap<String,String> onlineUsers,Boolean loggedIn){
        this.username=username;
        this.password=password;
        this.service=service;
        this.onlineUsers=onlineUsers;
        this.loggedIn=loggedIn;
    }

    @Override
    public Result handle() {

        if(username!=null && //check if username is valid
                password!=null && // check if password is valid
                !loggedIn && // check that the user not logged in
                !onlineUsers.contains(username) && // check that no one logged on this username
                service.CheckUsernameAndPassword(username,password)) // check password and username
            return new Result("ACK", "ACK login succeeded");

        return new Result("ERROR", "ERROR login failed");
    }

}
