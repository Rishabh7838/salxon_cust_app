package com.hello.one.x_cut.Model;

/**
 * Created by one on 5/5/2018.
 */

public class ParticularServiceModel {
    String Image;
    String Name;
    String Description;

    public ParticularServiceModel(String image, String name, String description) {
        Image = image;
        Name = name;
        Description = description;
    }

    public ParticularServiceModel() {
    }

    public String getParticularServiceImage() {
        return Image;
    }

    public void setParticularServiceImage(String image) {
        Image = image;
    }

    public String getParticularServiceName() {
        return Name;
    }

    public void setParticularServiceName(String name) {
        Name = name;
    }

    public String getParticularServiceDescription() {
        return Description;
    }

    public void setParticularServiceDescription(String description) {
        Description = description;
    }
}
