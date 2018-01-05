package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users;

public class user {

    private String username;
    private String type;
    private String password;
    private String country;
    private userMovie[] movies;
    private int balance;

    /**
     * getters
     */
    public String getUsername(){return username;}
    public String getPassword() {return password;}
    public String getType() {return type;}
    public String getCountry() {return country;}
    public userMovie[] getMovies() {return movies;}
    public int getBalance() {return balance;}

    /**
     * setters
     */
    public void setType(String type) {this.type = type;}
    public void setCountry(String country) {this.country = country;}
    public void addBalance(int addtobalance) {this.balance = balance + addtobalance;}
    public void decBalance(int dectobalance) {this.balance = balance - dectobalance;}

    public user(String username, String type, String password, String country){
        this.username = username;
        this.type = type;
        this.password = password;
        this.country = country;
        balance = 0;
        movies = new userMovie[0];
    }

    public Boolean hasmovie(String moviename){
        for(userMovie i: movies)if(i.getName().equals(moviename))return true;
        return false;
    }

    @Override
    public String toString() {
        String moviesString = "";
        for(userMovie i:movies)
            moviesString=moviesString+ (i.toString());

        return "{ username: " + username + ", type: " + type + ", password: " + password +
                ", country: " + country + " movies: "+ moviesString+ " }";
    }
}
