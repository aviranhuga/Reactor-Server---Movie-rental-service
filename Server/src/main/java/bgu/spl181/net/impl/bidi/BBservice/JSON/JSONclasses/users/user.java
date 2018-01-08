package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users;

import bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies.Movie;

/**
 * general class to represent user in the system
 */
public class user {

    private String username;
    private String type;
    private String password;
    private String country;
    private userMovie[] movies;
    private String balance;


    public user(String username, String type, String password, String country){
        this.username = username;
        this.type = type;
        this.password = password;
        this.country = country;
        balance = String.valueOf(0);
        movies = new userMovie[0];
    }

    /**
     * getters and setters
     */
    public String getUsername(){return username;}
    public String getPassword() {return password;}
    public String getType() {return type;}
    public String getCountry() {return country;}
    public int getBalance() {return Integer.parseInt(balance);}
    public void addBalance(int addtobalance) {this.balance = String.valueOf(Integer.parseInt(balance) + addtobalance);}
    public void decBalance(int dectobalance) {this.balance = String.valueOf(Integer.parseInt(balance) - dectobalance);}
    /**
     * checking if the user has the movie
     * @param moviename
     * @return
     */
    public Boolean hasmovie(String moviename){
        for(userMovie i: movies)if(i.getName().equals(moviename))return true;
        return false;
    }

    /**
     * add new movie to this user movies list
     * @param movie
     */
    public void addmovie(Movie movie){
        userMovie[] temp = new userMovie[movies.length+1];
        int j=0;
        for(userMovie i:movies)
            temp[j++] = i;
        temp[j] = new userMovie(movie.getid(),movie.getname());
        movies=temp;
    }

    /**
     * remove movie from the user movies list
     * @param moviename
     */
    public void removemovie(String moviename){
        userMovie[] temp = new userMovie[movies.length-1];
        int j=0;
        for(userMovie i:movies) {
            if (!(i.getName().equals(moviename)))
                temp[j++] = i;
        }
        movies=temp;
    }



}
