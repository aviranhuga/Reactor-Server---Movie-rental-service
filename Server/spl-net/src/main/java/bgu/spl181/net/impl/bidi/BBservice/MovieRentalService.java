package bgu.spl181.net.impl.bidi.BBservice;

import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies.Movie;
import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users.user;
import bgu.spl181.net.impl.bidi.BBservice.JSON.MoviesJSON;
import bgu.spl181.net.impl.bidi.BBservice.JSON.UsersJSON;
import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;

public class MovieRentalService  implements Service{

    private UsersJSON usershandler;
    private MoviesJSON movieshandler;
    private ReadWriteLock userlock;
    private ReadWriteLock movielock;

    public MovieRentalService(String userpath,String moviespath ,ReadWriteLock userslock, ReadWriteLock movieslock){
        usershandler = new UsersJSON(userpath,userslock);
        movieshandler = new MoviesJSON(moviespath,movieslock);
        this.userlock = userslock;
        this.movielock = movieslock;
    }
    @Override
    public void start() {
    }
    /**
     * register new user to the system
     * @param username
     * @param password
     * @param datablock
     * @return true if added and false otherwise
     */
    @Override
    public Boolean registerNewUser(String username, String password, ArrayList<String> datablock) {
        if(usershandler.hasUser(username) || datablock.size()!=1)return false;//check if user already exist

        String country = datablock.get(0);
        if(country.contains("country=\"") && country.substring(country.length()-1).equals("\"")){
            country=country.substring(9,country.length()-1);
            user newuser=new user(username,"normal",password,country);
            usershandler.adduser(newuser);
            return true;
        }

        return false;
    }
    /**
     * check if the username mach the password
     * @param username
     * @param password
     * @return
     */
    @Override
    public Boolean CheckUsernameAndPassword(String username, String password) {
        if(!usershandler.hasUser(username))return false;
        user tempUser = usershandler.getuser(username);
        if(tempUser.getPassword().equals(password))return true;
        return false;
    }
    /**
     * handle all request
     * @param name
     * @param username
     * @param parameters
     * @return
     */
    @Override
    public Result handleRequest(String name,String username, ArrayList<String> parameters) {
        Result result = null;
        switch (name) {
            case "balance info":
                result=handlebalanceinfo(username);
                break;
            case "balance add":
                result=handlebalaceadd(username,parameters);
                break;
            case "info":
                result=handleinfo(parameters);
                break;
            case "rent":
                result=handleinfo(parameters);
                break;

        }
        return result;
    }
    @Override
    public void end() {

    }

    /**
     * handle functions for specific requests
     */
    private Result handlebalanceinfo(String username) {
            String balance = String.valueOf(usershandler.getuser(username).getBalance());
            return new Result("ACK","ACK balance " + balance);
    }

    private Result handlebalaceadd(String username , ArrayList<String> parameters){
        if(parameters.size()==1) {
            user user = usershandler.getuser(username);
            String amount = parameters.get(0);
            user.addBalance(Integer.parseInt(amount));
            return new Result("ACK", "ACK balance " + user.getBalance() + " added "+amount);
        }
        return new Result("ERROR","ERROR request balance add failed");
    }

    private Result handleinfo(ArrayList<String> parameters) {
        if (parameters.size() == 0) {//no movie name
            String movieslist = "";
            Movie[] moviearray = movieshandler.getMovies();
            if (moviearray.length > 0) movieslist = moviearray[0].getname();
            for (int i = 1; i < moviearray.length; i++) {
                movieslist = movieslist + "," + moviearray[i].getname();
            }
            return new Result("ACK", "ACK info " + movieslist);
        } else if(movieshandler.hasMovie(parameters.get(0))){
            Movie movie = movieshandler.getMovie(parameters.get(0));
            return new Result("ACK","ACK info " + movie.getname() + " " + movie.getavailableAmount() + " " + movie.getprice() + " " + movie.getbannedCountries());
        }
        return new Result("ERROR","ERROR info failed");
    }

    private Result handlerent(String username, ArrayList<String> parameters){
        if(parameters.size()==1){
            String moviename = parameters.get(0);
            if(movieshandler.hasMovie(moviename)){
                Movie movie = movieshandler.getMovie(moviename);
                user user = usershandler.getuser(username);
                if(user.hasmovie(moviename) && movie.availbleInCountry(user.getCountry())){
                    userlock.writeLock().lock();
                    movielock.writeLock().lock();
                    user = usershandler.getuser(username);
                    movie = movieshandler.getMovie(moviename);
                    if(user.getBalance() >= movie.getprice() && movie.getavailableAmount()>0){
                        user.;

                        return new Result()
                    }

                }
            }
        }
    }

}
