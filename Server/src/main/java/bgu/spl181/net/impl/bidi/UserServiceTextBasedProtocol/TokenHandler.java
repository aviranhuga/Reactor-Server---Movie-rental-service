package bgu.spl181.net.impl.bidi.UserServiceTextBasedProtocol;

import java.util.ArrayList;

/**
 * @author TomHorvitz and AviraHuga
 * This class break a String to Diffrent tokens
 */
public class TokenHandler {

    private String token;
    private String commandname;
    private String password;
    private String name;
    private ArrayList<String> datablock;


    public TokenHandler(String token){
        this.token=token;
        commandname=null;
        password=null;
        name=null;
        datablock = new ArrayList<>();
    }
    /**
     * break the string into 3 parts, command name , first token and third.
     */
    public void Tokenize() {
        //if its an empty string
        if (token.isEmpty()) return;

        //get command name
        int i = token.indexOf(" ");
        if (i != -1) {
            commandname = token.substring(0, i);
            token = token.substring(i + 1);
        } else {
            commandname = token;
            return;
        }

        boolean endOfToken=false;
        int counter = 0;
        String next;

        while(!endOfToken) {

            counter++;
            i = token.indexOf(" ");
            if (i == -1) {
                endOfToken = true;
                next=token;
                token="";
            }
            else{
                next = token.substring(0, i);
                token = token.substring(i + 1);
            }

            switch (commandname) {

                case "REGISTER":
                    if(counter==1)name=next;
                    if(counter==2)password=next;
                    if(counter==3 && token.equals(""))parseDatablock(next);
                    else if(counter==3)parseDatablock(next + " " + token);
                    break;
                case "LOGIN":
                    if(counter==1)name=next;
                    if(counter==2)password=next;
                    break;
                case "REQUEST":
                    if(counter==1)name=next;
                    if(counter==2 && token.equals(""))parseDatablock(next);
                    else if(counter==2)parseDatablock(next + " " + token);
                    break;
            }


        }
    }


    public String getCommandname(){return commandname;}

    public String getName(){return name;}

    public String getPassword(){return password;}

    public ArrayList<String> getDataBlock(){return datablock;}


    private void parseDatablock(String data){
        Boolean hasNext=true;
        while(hasNext) {
            if(data.substring(0,1).equals("\"")){
                data = data.substring(1);
                int i = data.indexOf("\"");
                if (i == -1) return;
                else {
                    if(data.length()-1 == i){
                        this.datablock.add(data.substring(0, i));
                        return;
                    }
                    this.datablock.add(data.substring(0, i));
                    data = data.substring(i + 2);
                }
            }//end of "
            else {
                int i = data.indexOf(" ");
                if (i == -1) {
                    this.datablock.add(data);
                    hasNext = false;
                } else {
                    this.datablock.add(data.substring(0, i));
                    data = data.substring(i + 1);
                }
            }
        }//end of while
    }
}
