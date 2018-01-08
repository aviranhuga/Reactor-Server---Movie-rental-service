package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies;

/**
 * this class represent a movie in the system
 */
public class Movie {

        private String id;
        private String name;
        private String price;
        private String[] bannedCountries;
        private String availableAmount;
        private String totalAmount;

    public Movie(int id, String name,String[] bannedCountries, int price,int totalAmount){
        this.id = String.valueOf(id);
        this.name = name;
        this.price = String.valueOf(price);
        this.bannedCountries = bannedCountries;
        this.totalAmount = String.valueOf(totalAmount);
        availableAmount = String.valueOf(totalAmount);
    }
    /**
     * getters
     */
    public int getid(){return Integer.parseInt(id);}
    public String getprintingname() {return "\"" + name + "\"";}
    public String getname(){return name;}
    public int getprice() {return Integer.parseInt(price);}
    public int getavailableAmount() {return Integer.parseInt(availableAmount);}
    public int gettotalAmount() {return Integer.parseInt(totalAmount);}
     public String getbannedCountries() {
        String ans = "";
        if (bannedCountries.length > 0) ans = "\""+bannedCountries[0]+"\"";
        for (int i = 1; i < bannedCountries.length; i++) {
            ans = ans + "," + "\""+ bannedCountries[i] + "\"" ;
            }
            return ans;
    }/**
     * setters
     */
    public void decAvilableAmount() {this.availableAmount = String.valueOf(Integer.parseInt(availableAmount)-1);}
    public void addAvilableAmount() {this.availableAmount = String.valueOf(Integer.parseInt(availableAmount)+1);}
    public void setprice(int price) {this.price = String.valueOf(price);}
     public Boolean availbleInCountry(String countryname){
        for(String i: bannedCountries)
            if(i.equals(countryname))return false;
            return true;
        }
}
