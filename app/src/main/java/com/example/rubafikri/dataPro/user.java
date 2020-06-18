package com.example.rubafikri.dataPro;

public class user {
    private int uid;
    private String uname;
    private String email;
    private String password;
    public user(int uid ,String uname,String email,String password){
        this.uid = uid;
        this.uname = uname;
        this.email = email;
        this.password = password;
    }
    public int getId() {
        return uid;
    }

    public void setId(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
