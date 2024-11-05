package com.example.eventlottery;

import java.io.Serializable;

public class UsersList implements Serializable {
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
