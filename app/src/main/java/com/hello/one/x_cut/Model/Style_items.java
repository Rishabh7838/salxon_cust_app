package com.hello.one.x_cut.Model;

/**
 * Created by one on 3/12/2018.
 */

public class Style_items {
    private String Description,Discount,Image,ItemId,Name,Price;
    //private String Image,Name;

    public Style_items() {

    }

    public Style_items(String description, String discount, String image, String itemId, String name, String price) {
        Description = description;
        Discount = discount;
        Image = image;
        ItemId = itemId;
        Name = name;
        Price = price;
    }

    public Style_items(String image, String name) {
        Image = image;
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
