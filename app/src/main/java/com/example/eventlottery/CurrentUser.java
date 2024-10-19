package com.example.eventlottery;

public class CurrentUser {
    private String fName;
    private String lName;
    private String email;
    private boolean isAdmin;

    public CurrentUser(String fName, String lName, String email, boolean isAdmin) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isAdmin = isAdmin;
    }
    public String getfName(){
        return fName;
    }
    public String getlName(){
        return lName;
    }
    public String getEmail(){
        return email;
    }
    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
