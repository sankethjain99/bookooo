package com.example.bookooo;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String IsBookii;

    public User() {
    }



    public User(String name, String password) {
        Name = name;
        Password = password;
        IsBookii = "false";

    }

    public String getIsBookii() {
        return IsBookii;
    }

    public void setIsBookii(String isBookii) {
        IsBookii = isBookii;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
