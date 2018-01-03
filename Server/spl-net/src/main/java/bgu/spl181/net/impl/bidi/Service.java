package bgu.spl181.net.impl.bidi;

/**
 * @author TomHorvitz and AviraHuga
 */
public interface Service {

    void start();

    Result handleRegister(String username, String password, String datablock);

    Result handleLogin(String username, String password);

    Result handleSignOut();

    Result handleRegister(String username, String datablock);

    void end();
}
