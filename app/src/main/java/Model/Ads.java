package Model;

public class Ads {

    private String title;
    private String description;
    private String user;
    //private String category;
    private double price;
    /*private double latitude;
    private double langitude;
    private double customerRating;
    private double sellerRating;*/

    public Ads(String title, String description, String user, double price) {
        this.title = title;
        this.description = description;
        this.user = user;
      //  this.category = category;
        this.price = price;
    }


    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public double getPrice() {
        return price;
    }

    //Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
