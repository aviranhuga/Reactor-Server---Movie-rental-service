package bgu.spl181.net.impl.bidi.BBservice.JSON;

import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users.user;
import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users.users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * users json file handler
 */
public class UsersJSON {

    private users users_;
    private Gson gson;
    private String path;
    private ReadWriteLock readWriteLock;

    public UsersJSON(String path, ReadWriteLock readWriteLock){
        this.path=path;
        this.readWriteLock=readWriteLock;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.setPrettyPrinting();//Sets pretty formatting
        gson = gsonBuilder.create();
    }
    /**
     * get the users list
     * @return users list from the json file
     */
    private void getFromJson() {

        try (Reader reader = new FileReader(path)) {
            users_ = gson.fromJson(reader, users.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * get the users list
     * @return users list from the json file
     */
    private void updateJson() {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(users_,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * get a specific user from json file
     * @param username the name of the user
     * @return user if the username exist in the system else return null
     */
    public user getuser(String username){
        readWriteLock.readLock().lock();
        getFromJson();
        user temp = users_.getUser(username);
        readWriteLock.readLock().unlock();
        return temp;
    }
    /**
     * add new user to the json file
     * @param newuser
     */
    public void adduser(user newuser){
        readWriteLock.writeLock().lock();
        getFromJson();
        users_.add(newuser);
        updateJson();
        readWriteLock.writeLock().unlock();
    }
    /**
     * check if user exist in json file
     * @param username
     * @return
     */
    public Boolean hasUser(String username){
        readWriteLock.readLock().lock();
        getFromJson();
        Boolean ans =  users_.hasUser(username);
        readWriteLock.readLock().unlock();
        return ans;
    }
    public void rentmovie(String username, String moviename, int amount){
        getFromJson();
        users_.getUser(username).decBalance(amount);
        users_.getUser(username).addmovie(moviename);
    }


}
