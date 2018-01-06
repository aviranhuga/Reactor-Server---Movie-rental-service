package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.USTBCommands;

import bgu.spl181.net.api.bidi.CommandInterface;
import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

public abstract class USTBCommand implements CommandInterface {

    protected Service service;
    protected Boolean loggedIn;

}
