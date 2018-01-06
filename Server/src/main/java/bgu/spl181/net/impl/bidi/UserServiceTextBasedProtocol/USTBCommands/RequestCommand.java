package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands;

import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

import java.util.ArrayList;

public class RequestCommand extends USTBCommand {

    protected String username;
    protected String name;
    protected ArrayList<String> datablock;
    protected Service service;

    public RequestCommand(String username,String name, ArrayList<String> datablock, Boolean loggedIn,Service service){
        this.name = name;
        this.datablock=datablock;
        this.loggedIn = loggedIn;
        this.service=service;
        this.username=username;
    }

    @Override
    public Result handle() {

        if(loggedIn)
            return service.handleRequest(name,username,datablock);

        return new Result("ERROR","ERROR request "+ name +" failed");
    }
}
