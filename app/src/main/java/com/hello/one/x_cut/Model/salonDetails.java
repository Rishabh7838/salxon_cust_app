package com.hello.one.x_cut.Model;

/**
 * Created by one on 3/30/2018.
 */

public class salonDetails {
    private String Image;
    private String Name;
    private String Description;
    private String Rating;

    public salonDetails() {
    }

    public salonDetails(String image, String name, String description, String rating) {
        Image = image;
        Name = name;
        Description = description;
        Rating = rating;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}
