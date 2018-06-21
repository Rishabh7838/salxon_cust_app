package com.hello.one.x_cut.Model;

/**
 * Created by one on 3/11/2018.
 */

public class Category {

    private String Name;
    private String Image;

    public Category()
    {

    }

    public Category(String name, String Image) {
        Name = name;
        this.Image = Image;
    }


    //setting Image and name
    public void setName(String name) {
        Name = name;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    // getting Image and name


    public String getName() {
        return Name;
    }

    public String getImage() {
        return Image;
    }




}
