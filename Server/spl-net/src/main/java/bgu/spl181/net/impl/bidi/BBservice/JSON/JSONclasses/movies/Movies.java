package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies;


public class Movies {

    private Movie[] movies;

    /**
     * add new movie to the movies list
     * @param newmovie
     */
    public void add(Movie newmovie){

        if(hasMovie(newmovie.getname()))return;

        Movie[] newlist = new Movie[movies.length+1];
        int j=0;
        for(Movie i:movies)
            newlist[j++]=i;
        newlist[j]=newmovie;
        movies=newlist;
    }

    /**
     * remove movie from movies
     * @param moviename
     */
    public void remove(String moviename){
        if(!hasMovie(moviename))return;

        Movie[] newlist = new Movie[movies.length-1];
        int j=0;
        for(Movie i:movies)
            if(!(i.getname().equals(moviename)))newlist[j++]=i;
        movies=newlist;
    }

    /**
     * check if movie exist
     * @param moviename
     * @return true if exist and false otherwise
     */
    public Boolean hasMovie(String moviename){
        for(Movie i:movies)
            if(i.getname().equals(moviename))return true;
        return false;
    }

    /**
     * get specific movie
     * @param moviename
     * @return the requested movie if exist and null otherwise
     */
    public Movie getMovie(String moviename){
        for(Movie i:movies)
            if(i.getname().equals(moviename))return i;
        return null;
    }

    /**
     * get movie list
     * @return movies
     */
    public Movie[] getMovies(){return movies;}

}
