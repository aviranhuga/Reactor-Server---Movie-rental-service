package bgu.spl181.net.impl.bidi.BBservice.JSON;

import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies.Movie;
import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies.Movies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;

public class MoviesJSON {

    private Movies movies_;
    private Gson gson;
    private String path;
    private ReadWriteLock readWriteLock;

    public MoviesJSON(String path, ReadWriteLock readWriteLock){
        this.path=path;
        this.readWriteLock=readWriteLock;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.setPrettyPrinting();//Sets pretty formatting
        gson = gsonBuilder.create();
    }
    /**
     * update the movies_ copy to be the one from JSON
     */
    private void getFromJson () {
        try (Reader reader = new FileReader(path)) {
            movies_ = gson.fromJson(reader, Movies.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * update the json file to be movies_
     */
    private void updateJson() {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(movies_,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Check if the movie is in the system
     * @param moviename
     */
    public Boolean hasMovie(String moviename) {
        readWriteLock.readLock().lock();
        getFromJson();
        Boolean ans = movies_.hasMovie(moviename);
        readWriteLock.readLock().unlock();
        return ans;
    }
    /**
     * get a specific movie from json file
     * @param moviename
     * @return
     */
    public Movie getMovie(String moviename){
        readWriteLock.readLock().lock();
        getFromJson();
        Movie movie = movies_.getMovie(moviename);
        readWriteLock.readLock().unlock();
        return movie;
    }
    /**
     * get movies list
     * @return
     */
    public Movie[] getMovies(){
        readWriteLock.readLock().lock();
        getFromJson();
        Movie[] movies = movies_.getMovies();
        readWriteLock.readLock().unlock();
        return movies;
    }
    /**
     * rent a movie function
     * @param moviename
     * @param balance
     * @param country
     * @return the movie or null otherwise
     */
    public Movie rentmovie(String moviename,int balance,String country){
        readWriteLock.writeLock().lock();
        Movie rentmovie=null;
        getFromJson();
        Movie movie =movies_.getMovie(moviename);
        if(movie!=null &&
                movie.getprice()<balance &&
                movie.availbleInCountry(country) &&
                movie.getavailableAmount()>0) {
            movie.decAvilableAmount();
            rentmovie=movie;
            updateJson();
        }//end of if
        readWriteLock.writeLock().unlock();
        return rentmovie;
    }
    /**
     * return a movie function
     * @param moviename
     * @return the movie
     */
    public Movie returnmovie(String moviename){
        readWriteLock.writeLock().lock();
        getFromJson();
        Movie movie =movies_.getMovie(moviename);
        movie.addAvilableAmount();
        updateJson();
        readWriteLock.writeLock().unlock();
        return movie;
    }
    /**
     * add new movie to the Json file
     * @param moviename
     * @param amount
     * @param price
     * @param bannedcountry
     * @return the Movie or false otherwise
     */
    public Movie addmovie(String moviename, int amount, int price, String[] bannedcountry){
        Movie movie = null;
        readWriteLock.writeLock().lock();
        getFromJson();
        if(!movies_.hasMovie(moviename)){
            movie = new Movie(movies_.getnextid(),moviename,bannedcountry,price,amount);
            movies_.add(movie);
            updateJson();
        }
        readWriteLock.writeLock().unlock();
        return movie;
    }

    /**
     * remove a movie from the system
     * @param moviename
     * @return true if the movie deleted.
     */
    public Boolean remmovie(String moviename){
        readWriteLock.writeLock().lock();
        getFromJson();
        boolean ans=false;
        Movie movie = movies_.getMovie(moviename);
        if(movie!=null && movie.gettotalAmount()==movie.getavailableAmount()) {
            movies_.remove(moviename);
            updateJson();
            ans=true;
        }
        readWriteLock.writeLock().unlock();
        return ans;
    }

    /**
     * chaning price of a specific movie
     * @param moviename movie name
     * @param price price
     * @return movie if succeed and null otherwise
     */
    public Movie changeprice(String moviename, int price){
        readWriteLock.writeLock().lock();
        getFromJson();
        Movie movie = movies_.getMovie(moviename);
        if(movie != null){
            movie.setprice(price);
            updateJson();
        }
        readWriteLock.writeLock().unlock();
        return movie;
    }


}
