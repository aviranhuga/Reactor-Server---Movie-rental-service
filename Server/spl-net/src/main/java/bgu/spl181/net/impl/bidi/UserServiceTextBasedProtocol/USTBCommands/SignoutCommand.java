package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands;

import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;


public class SignoutCommand extends USTBCommand  {


    public SignoutCommand(Boolean loggedIn){
        this.loggedIn=loggedIn;

    }
    @Override
    public Result handle() {
        if(!loggedIn)return new Result("ERROR","ERROR signout failed");
        return new Result("ACK","ACK signout succeeded");
    }
}
