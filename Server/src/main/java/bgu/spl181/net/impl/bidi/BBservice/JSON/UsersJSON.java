package bgu.spl181.net.impl.bidi.BBservice.JSON;

import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies.Movie;
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
     * add a new user to JSON file
     * @param newuser the new user
     * @return true if added and false otherwise
     */
    public Boolean adduser(user newuser){
        readWriteLock.writeLock().lock();
        getFromJson();
        Boolean ans = !(users_.hasUser(newuser.getUsername()));
        if(ans){
            users_.add(newuser);
            updateJson();
        }
        readWriteLock.writeLock().unlock();
        return ans;
    }
    /**
     * check if user exist in json file
     * @param username
     * @return true if the username mach the password and false other wise
     */
    public Boolean CheckUsernameAndPassword(String username,String password){
        Boolean ans=false;
        readWriteLock.readLock().lock();
        getFromJson();
        if(users_.hasUser(username))
             ans = users_.getUser(username).getPassword().equals(password);
        readWriteLock.readLock().unlock();
        return ans;
    }

    /**
     * add a amount to the username balance
     * @param username
     * @param amount
     * @return the current balance of the username
     */
    public int addbalance(String username, int amount){
        readWriteLock.writeLock().lock();
        getFromJson();
        users_.getUser(username).addBalance(amount);
        int ans = users_.getUser(username).getBalance();
        updateJson();
        readWriteLock.writeLock().unlock();
        return ans;
    }

    /**
     * rent a new movie, this function also use movie json
     * @param username
     * @param moviename
     * @param movieshandler
     * @return the movie
     */
    public Movie rentmovie(String username, String moviename, MoviesJSON movieshandler ){
        readWriteLock.writeLock().lock();
        getFromJson();
        Movie movie = null;
        user user = users_.getUser(username);
        if(!user.hasmovie(moviename)) {
            movie = movieshandler.rentmovie(moviename, user.getBalance(), user.getCountry());
            if(movie!=null){
                user.decBalance(movie.getprice());
                user.addmovie(moviename);
                updateJson();
            }
        }
        readWriteLock.writeLock().unlock();
        return movie;
    }

    /**
     * return a movie, this function also use movie json
     * @param username
     * @param moviename
     * @param movieshandler
     * @return the movie
     */
    public Movie returnmovie(String username, String moviename, MoviesJSON movieshandler ){
        readWriteLock.writeLock().lock();
        getFromJson();
        Movie movie = null;
        user user = users_.getUser(username);
        if(user.hasmovie(moviename)) {
            movie = movieshandler.returnmovie(moviename);
            user.removemovie(moviename);
            updateJson();
        }
        readWriteLock.writeLock().unlock();
        return movie;
    }

    /**
     * check if a specific user is an admin
     * @param username
     * @return true if admin, false otherwise
     */
    public Boolean checkifadmin(String username){
        Boolean ans;
        readWriteLock.readLock().lock();
        getFromJson();
        ans = users_.getUser(username).getType().equals("admin");
        readWriteLock.readLock().unlock();
        return ans;

    }

}
