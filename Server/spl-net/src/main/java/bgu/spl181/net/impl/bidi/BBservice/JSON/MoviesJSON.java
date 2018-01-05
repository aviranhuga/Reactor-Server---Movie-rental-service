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
        readWriteLock.readLock().lock();
        return movie;
    }

    /**
     * get movie list
     * @return
     */
    public Movie[] getMovies(){
        readWriteLock.readLock().lock();
        getFromJson();
        Movie[] movies = movies_.getMovies();
        readWriteLock.readLock().unlock();
        return movies;
    }

    public void rentmovie(String moviename){
        getFromJson();
        movies_.getMovie(moviename).setavailableAmount(movies_.getMovie(moviename).gettotalAmount()-1);
    }


}
