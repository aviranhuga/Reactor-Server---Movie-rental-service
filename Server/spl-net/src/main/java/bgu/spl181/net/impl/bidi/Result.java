package bgu.spl181.net.impl.bidi;

/**
 * This class represent result of a command.
 */
public class Result {
    private String type;
    private String message;


    public Result(String type, String message){
        this.type = type;
        this.message=message;
    }
    public String getType(){return type;}

    public String getMessage(){return message;}
}
