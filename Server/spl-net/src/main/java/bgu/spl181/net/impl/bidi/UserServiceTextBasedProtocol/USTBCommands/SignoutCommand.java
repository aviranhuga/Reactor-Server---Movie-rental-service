package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands;

import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;


public class SignoutCommand extends USTBCommand  {


    public SignoutCommand(Boolean loggedIn){
        this.loggedIn=loggedIn;

    }
    @Override
    public Result handle() {
        if(!loggedIn)result.setResult("ERROR","ERROR signout failed");
        else result.setResult("ACK","ACK signout succeeded");
        return result;
    }
}
