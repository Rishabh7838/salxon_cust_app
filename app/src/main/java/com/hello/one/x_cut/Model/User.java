package com.hello.one.x_cut.Model;

/**
 * Created by one on 3/11/2018.
 */

public class User {

    private  String name;
    private  String password;
    private  String number;
    private  String gender;
    private  String age;
    private String ProfileImageUrl;

    public User(){

    }



    public User(String name, String password, String number, String gender, String age , String ProfileImageUrl) {
        this.name = name;
        this.password = password;
        this.number = number;
        this.gender = gender;
        this.age = age;
        this.ProfileImageUrl = ProfileImageUrl;
    }

    public User(String name, String password, String number, String gender, String age ) {
        this.name = name;
        this.password = password;
        this.number = number;
        this.gender = gender;
        this.age = age;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName()
    {
        return name;

    }

    public String getPassword()
    {
        return password;

    }
    public void setName(String name)
    {
        this.name = name;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        ProfileImageUrl = profileImageUrl;
    }
}
