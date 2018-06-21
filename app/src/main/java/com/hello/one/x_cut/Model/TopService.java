package com.hello.one.x_cut.Model;

/**
 * Created by one on 5/5/2018.
 */

public class TopService {
    String ImageUrl;
    String Name;
    String Description;

    public TopService() {
    }

    public TopService(String ImageUrl, String Name, String Description) {
        this.ImageUrl = ImageUrl;
        this.Name = Name;
        this.Description = Description;
    }

    public String getTopServiceImage() {
        return ImageUrl;
    }

    public void setTopServiceImage(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getTopServiceName() {
        return Name;
    }

    public void setTopServiceName(String Name) {
        this.Name = Name;
    }

    public String getTopServiceDescription() {
        return Description;
    }

    public void setTopServiceDescription(String Description) {
        this.Description = Description;
    }
}
