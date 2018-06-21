package com.hello.one.x_cut.Model;

/**
 * Created by one on 4/1/2018.
 */

public class findSalonLayout {
    private String name;
    private String address;
    private String salon_type;
    private String imageUrl;
    //private String total_amount;
    private String phone_no;

    public findSalonLayout() {
    }

    public findSalonLayout(String name, String address, String salon_type, String imageUrl, String phone_no  ) {
        this.name = name;
        this.address = address;
        this.salon_type = salon_type;
        this.phone_no = phone_no;
        this.imageUrl = imageUrl;
      //  this.total_amount = total_amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

//    public String getTotal_amount() {
//        return total_amount;
//    }
//
//    public void setTotal_amount(String total_amount) {
//        this.total_amount = total_amount;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalon_type() {
        return salon_type;
    }

    public void setSalon_type(String salon_type) {
        this.salon_type = salon_type;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
