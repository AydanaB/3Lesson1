package com.example.a3lesson1;

public class Contacts {

    public String name, phoneNum;

    public Contacts(){

    }
    public Contacts(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
