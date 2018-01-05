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
        Result temp;
        if(loggedIn) {
            temp = service.handleRequest(name,username,datablock);
            if(temp.getBooleanResult())return temp;
        }
        result.setResult("ERROR","ERROR request "+ name +" failed");
        return result;
    }
}
