package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.users;

public class users {

    private user[] users;

    /**
     * add new user to user list
     * @param newuser
     */
    public void add(user newuser){

        if(hasUser(newuser.getUsername()))return;

        user[] newlist = new user[users.length+1];
        int j=0;
        for(user i:users)
            newlist[j++]=i;
        newlist[j]=newuser;
        users=newlist;
    }

    /**
     * remove user from user
     * @param username
     */
    public void remove(String username){
        if(!hasUser(username))return;
        user[] newlist = new user[users.length-1];
        int j=0;
        for(user i:users)
            if(!(i.getUsername().equals(username)))newlist[j++]=i;
        users=newlist;
    }

    /**
     * check if user exist
     * @param username
     * @return true if exist and false otherwise
     */
    public Boolean hasUser(String username){
        for(user i:users)
            if(i.getUsername().equals(username))return true;
        return false;
    }

    /**
     * get specific user
     * @param username
     * @return the requested user if exist and null otherwise
     */
    public user getUser(String username){
        for(user i:users)
            if(i.getUsername().equals(username))return i;
        return null;
    }





    /**
     *
     * @return String with all the info about the users
     */
    @Override
    public String toString() {
        String ans="";
       for(int i=0 ; i < users.length ; i++)
           ans=ans+users[i].toString();
       return ans;
    }
}
