package com.example.eventlottery;

public class UsersList {
    private String iD;
    private String name;

    public UsersList() {
    }

    public UsersList(String iD, String name) {
        this.iD = iD;
        this.name = name;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }
}
