package com.hello.one.x_cut.Model;

/**
 * Created by one on 3/27/2018.
 */

public class SalonServices {

    private String Name,ImageUrl,Description,Offer;

    public SalonServices() {
    }

    public SalonServices(String name, String imageUrl, String description, String offer) {
        Name = name;
        ImageUrl = imageUrl;
        Description = description;
        Offer = offer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getOffer() {
        return Offer;
    }

    public void setOffer(String offer) {
        Offer = offer;
    }
}
