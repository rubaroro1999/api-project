package com.example.rubafikri.dataPro;

public class proudct {

    private int pid ;
    private String name ;
    private String price;
    private String key;

    public proudct(int pid , String name, String price ,String key){
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.key = key;

    }
    public int getPid() {
        return pid;
    }

    public void getPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
