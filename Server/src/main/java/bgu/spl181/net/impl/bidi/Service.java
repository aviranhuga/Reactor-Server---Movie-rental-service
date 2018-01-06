package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

import java.util.ArrayList;

/**
 * @author TomHorvitz and AviraHuga
 */
public interface Service {

    void start();

    /**
     * try to register new user.
     */
    public Boolean registerNewUser(String username,String password,ArrayList<String> datablock);
    /**
     * check if the username and password fit
     */
    public Boolean CheckUsernameAndPassword(String username,String password);
    /**
     * handle requests
     */
    public Result handleRequest(String name,String username,ArrayList<String> parameters);

    void end();
}
