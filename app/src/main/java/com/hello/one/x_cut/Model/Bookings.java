package com.hello.one.x_cut.Model;

/**
 * Created by one on 3/14/2018.
 */

public class Bookings {

    private String Quantity;
    private String Service_Name;
    private String TimeStamp;
    private String Price;
    private String Description;


    public Bookings() {
    }

    public Bookings(String Quantity, String service_Name, String timeStamp, String price, String disscount) {
        this.Quantity = Quantity;
        Service_Name = service_Name;
        TimeStamp = timeStamp;
        Price = price;
        Description = disscount;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getService_Name() {
        return Service_Name;
    }

    public void setService_Name(String service_Name) {
        Service_Name = service_Name;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String disscount) {
        Description = disscount;
    }
}
