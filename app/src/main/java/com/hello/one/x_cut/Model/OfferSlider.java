package com.hello.one.x_cut.Model;

/**
 * Created by one on 6/17/2018.
 */

public class OfferSlider {
    private String Id,Name,Image;

    public OfferSlider(String id, String name, String image) {
        Id = id;
        Name = name;
        Image = image;
    }

    public OfferSlider() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
