package bgu.spl181.net.impl.bidi.BBservice;

import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies.Movie;
import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users.user;
import bgu.spl181.net.impl.bidi.BBservice.JSON.MoviesJSON;
import bgu.spl181.net.impl.bidi.BBservice.JSON.UsersJSON;
import bgu.spl181.net.impl.bidi.Service;
import bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol.Result;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;

public class MovieRentalService  implements Service {

    private UsersJSON usershandler;
    private MoviesJSON movieshandler;

    public MovieRentalService(String userpath, String moviespath, ReadWriteLock userslock, ReadWriteLock movieslock) {
        usershandler = new UsersJSON(userpath, userslock);
        movieshandler = new MoviesJSON(moviespath, movieslock);
    }

    @Override
    public void start() {
    }

    /**
     * register new user to the system
     *
     * @param username
     * @param password
     * @param datablock
     * @return true if added and false otherwise
     */
    @Override
    public Boolean registerNewUser(String username, String password, ArrayList<String> datablock) {
        if (datablock.size() != 1) return false; //check if user already exist
        //get the country name
        String country = datablock.get(0);
        if (country.contains("country=\"") && country.substring(country.length() - 1).equals("\"")) {
            country = country.substring(9, country.length() - 1);
            user newuser = new user(username, "normal", password, country);
            return usershandler.adduser(newuser);
        }
        return false;//country syntax is not right
    }

    /**
     * check if the username mach the password
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Boolean CheckUsernameAndPassword(String username, String password) {
        return usershandler.CheckUsernameAndPassword(username, password);
    }

    /**
     * handle all request
     *
     * @param name
     * @param username
     * @param parameters
     * @return
     */
    @Override
    public Result handleRequest(String name, String username, ArrayList<String> parameters) {
        Result result = null;
        switch (name) {
            case "balance info":
                result = handlebalanceinfo(username);
                break;
            case "balance add":
                result = handlebalaceadd(username, parameters);
                break;
            case "info":
                result = handleinfo(parameters);
                break;
            case "rent":
                result = handlerent(username, parameters);
                break;
            case "return":
                result = handlereturn(username, parameters);
                break;
            case "addmovie":
                result = handleaddmovie(username, parameters);
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
        return new Result("ACK", "ACK balance " + balance);
    }

    private Result handlebalaceadd(String username, ArrayList<String> parameters) {
        if (parameters.size() == 1) {
            String amount = parameters.get(0);
            return new Result("ACK", "ACK balance " + String.valueOf(usershandler.addbalance(username, Integer.parseInt(amount))) + " added " + amount);
        }
        return new Result("ERROR", "ERROR request balance add failed");
    }

    private Result handleinfo(ArrayList<String> parameters) {
        if (parameters.size() == 0) {//no movie name, get all movies
            String movieslist = "";
            Movie[] moviearray = movieshandler.getMovies();
            if (moviearray.length > 0) movieslist = moviearray[0].getname();
            for (int i = 1; i < moviearray.length; i++) {
                movieslist = movieslist + " " + moviearray[i].getname();
            }
            return new Result("ACK", "ACK info " + movieslist);
            //if there is a movie name
        } else {
            Movie movie = movieshandler.getMovie(parameters.get(0));
            if (movie != null)
                return new Result("ACK", "ACK info " + movie.getname() + " " + String.valueOf(movie.getavailableAmount()) + " " + String.valueOf(movie.getprice()) + " " + movie.getbannedCountries());
        }
        return new Result("ERROR", "ERROR request info failed");
    }

    private Result handlerent(String username, ArrayList<String> parameters) {
        if (parameters.size() == 1) { // check if we have the movie name
            String moviename = parameters.get(0);
            Movie movie = usershandler.rentmovie(username, moviename, this.movieshandler);
            if (movie != null) {
                return new Result("ACK", "ACK rent " + moviename + " success", "BROADCAST movie " + moviename + " " + String.valueOf(movie.getavailableAmount()) + " " + String.valueOf(movie.getprice()));
            }
        }
        return new Result("ERROR", "ERROR request rent failed");
    }

    private Result handlereturn(String username, ArrayList<String> parameters) {
        if (parameters.size() == 1) { // check if we have the movie name
            String moviename = parameters.get(0);
            Movie movie = usershandler.rentmovie(username, moviename, this.movieshandler);
            if (movie != null) {
                return new Result("ACK", "ACK return " + moviename + " success", "BROADCAST movie " + moviename + " " + String.valueOf(movie.getavailableAmount()) + " " + String.valueOf(movie.getprice()));
                }
           }
              return new Result("ERROR", "ERROR request return failed");
        }

    private Result handleaddmovie(String username,ArrayList<String> parameters ){
        if(usershandler.checkifadmin(username)&&
                parameters.size()>2){
            String[] bannedcountryArray = new String[parameters.size()-3];
            for(int i=3 ; i<bannedcountryArray.length ; i++)
                bannedcountryArray[i-3]=parameters.get(i);
            Movie movie = movieshandler.addmovie(parameters.get(0),Integer.parseInt(parameters.get(1)),Integer.parseInt(parameters.get(2)),bannedcountryArray);
            if(movie!=null){
                return new Result("ACK", "ACK addmovie " + movie + " success", "BROADCAST movie " + movie + " " + String.valueOf(movie.getavailableAmount()) + " " + String.valueOf(movie.getprice()));
            }
        }
        return new Result("ERROR", "ERROR request addmovie failed");
    }


}