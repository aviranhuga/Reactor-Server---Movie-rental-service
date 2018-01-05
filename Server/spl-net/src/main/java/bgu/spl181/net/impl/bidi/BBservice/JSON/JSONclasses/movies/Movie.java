package bgu.spl181.net.impl.bidi.BBservice.JSON.JSONclasses.movies;

public class Movie {

        private int id;
        private String name;
        private int price;
        private String[] bannedCountries;
        private int availableAmount;
        private int totalAmount;

        /**
         * getters
         */
        public int getid(){return id;}
        public String getname() {return name;}
        public int getprice() {return price;}
        public int getavailableAmount() {return availableAmount;}
        public int gettotalAmount() {return totalAmount;}

         public String getbannedCountries() {
             String ans = "";
              if (bannedCountries.length > 0) ans = bannedCountries[0];
             for (int i = 1; i < bannedCountries.length; i++) {
                 ans = ans + "," + bannedCountries[i];
               }
              return ans;
         }
        /**
         * setters
         */
        public void settotalAmount(int totalAmount) {this.totalAmount = totalAmount;}
        public void setavailableAmount(int availableAmount) {this.availableAmount = availableAmount;}
        public void setprice(int price) {this.price = price;}

        public Boolean availbleInCountry(String countryname){
            for(String i: bannedCountries)if(i.equals(countryname))return false;
            return true;
        }


        public Movie(int id, String name,String[] bannedCountries, int price,int totalAmount){
            this.id = id;
            this.name = name;
            this.price = price;
            this.bannedCountries = bannedCountries;
            this.totalAmount = totalAmount;
            availableAmount = totalAmount;
            }


}
