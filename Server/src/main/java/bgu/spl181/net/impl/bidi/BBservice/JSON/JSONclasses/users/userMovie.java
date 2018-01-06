package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users;

public class userMovie {

    private int id;
    private String name;

    public int getId() {return id;}

    public String getName(){return name;}

    public userMovie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Id: " + id + ", name: " +name;
    }
}
