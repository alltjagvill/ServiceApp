package Model;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Ads implements Serializable {

    private String title;
    private String description;
    private String userID;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Double price;
    private Double userLat;
    private Double userLon;

    // private String id;
    //private String category;
    /*private double latitude;
    private double langitude;
    private double customerRating;
    private double sellerRating;*/

   /* public void setId(String id) {
        this.id = id;
    }*/
    public Ads() {

    }
    public Ads(String title, String description, String userID, String firstName, String lastName, String userEmail, Double price, Double userLat, Double userLon) {
        this.title = title;
        this.description = description;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.price = price;
        this.userLat = userLat;
        this.userLon = userLon;

     //   this.category = category;

    }


    //Getters
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUserID() {return userID;}
    public String getUserEmail() {return  userEmail;}
    public Double getPrice() { return price;
    }
    public Double getUserLat() { return userLat;
    }

    public Double getUserLon() { return userLon;
    }
    //Setters

    /*public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(String user) {
        this.user = user;
    }*/

    /*public void setPrice(double price) {
        this.price = price;
    }*/
}
