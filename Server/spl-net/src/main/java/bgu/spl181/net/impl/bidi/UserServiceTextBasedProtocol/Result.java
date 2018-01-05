package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol;

/**
 * This class represent result of a command.
 */
public class Result {

    private String type;
    private String message;
    private Boolean hasResult;
    private Boolean result;



    public Result(){
        this.type = "";
        this.message="";
        hasResult=false;
        result=false;
    }

    public Result(String type, String message){
        setResult(type,message);
    }

    public void setResult(String type, String message){
        this.type=type;
        this.message=message;
        hasResult=true;
    }

    public void setBooleanResult(Boolean result){
        this.result=result;
        hasResult=true;
    }
    public String getType(){return type;}

    public String getMessage(){return message;}

    public Boolean getBooleanResult(){return result;}

    public Boolean hasResult(){
        return hasResult;
    }
}
