package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands;

import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

import java.util.ArrayList;

public class RegisterCommand extends USTBCommand {

    protected String username;
    protected String password;
    protected ArrayList<String> datablock;

    public RegisterCommand(String username, String password, ArrayList<String> datablock, Service service,Boolean loggedIn){
        this.username=username;
        this.password=password;
        this.datablock=datablock;
        this.service=service;
        this.loggedIn=loggedIn;
    }

    @Override
    public Result handle() {

        if(username!=null && //valid username
                password!=null && // valid password
                (!loggedIn) &&
                service.registerNewUser(username,password,datablock)) //check with service
            result.setResult("ACK", "ACK registration succeeded");
        else
            result.setResult("ERROR", "ERROR registration failed");

        System.out.println(result.getMessage());
        return result;
    }

}
