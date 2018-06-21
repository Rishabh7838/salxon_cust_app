package com.hello.one.x_cut.Model;

/**
 * Created by one on 4/14/2018.
 */

public class Salon_Service {
    private String service_name;
    private String total_amount;

    public Salon_Service() {
    }

    public Salon_Service(String service_name, String total_amount) {
        this.service_name = service_name;
        this.total_amount = total_amount;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}
