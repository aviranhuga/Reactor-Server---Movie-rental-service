package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol;

/**
 * This class represent result of a command.
 */
public class Result {

    private String type;
    private String message;
    private String broadcast;
    private Boolean hasbroadcast;

    public Result(String type, String message){
        this.type=type;
        this.message=message;
        broadcast=null;
        hasbroadcast=false;
    }

    public Result(String type, String message , String broadcast){
        this.type=type;
        this.message=message;
        this.broadcast=broadcast;
        hasbroadcast=true;
    }

    public String getType(){return type;}

    public String getMessage(){return message;}

    public String getBroadcast(){return broadcast;}

    public Boolean hasBroadcast(){return hasbroadcast;}


}
