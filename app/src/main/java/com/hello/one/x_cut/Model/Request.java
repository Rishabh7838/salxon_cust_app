package com.hello.one.x_cut.Model;

import java.util.List;

/**
 * Created by one on 3/16/2018.
 */

public class Request {

    private String Phone,name,total,status;
    private List<Bookings> style;

    public Request() {
    }

    public Request(String phone, String name, String total, List<Bookings> style) {
        Phone = phone;
        this.name = name;
        this.total = total;
        this.style = style;
        this.status = "0"; //Default 0. 0=service booked,1=payment done 2=service recieved
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Bookings> getStyle() {
        return style;
    }

    public void setStyle(List<Bookings> style) {
        this.style = style;
    }
}
