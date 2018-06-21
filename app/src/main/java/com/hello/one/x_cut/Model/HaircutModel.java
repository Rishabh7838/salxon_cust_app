package com.hello.one.x_cut.Model;

/**
 * Created by one on 5/10/2018.
 */

public class HaircutModel {
    String Name;

    public HaircutModel(String name) {
        Name = name;
    }

    public HaircutModel() {
    }

    public String getHairCutName() {
        return Name;
    }

    public void setHairCutName(String name) {
        Name = name;
    }
}
