package com.example.home_connect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appliance {


    @SerializedName("name")
    private  String name;

    @SerializedName("brand")
    private  String brand;

    @SerializedName("vib")
    private  String vib;

    @SerializedName("connected")
    private  boolean connected;

    @SerializedName("type")
    private  String type;

    @SerializedName("enumber")
    private  String enumber;

    @SerializedName("haId")
    private  String haId;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVib() {
        return vib;
    }

    public void setVib(String vib) {
        this.vib = vib;
    }

    public String getConnected() {
        return String.valueOf(connected);
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnumber() {
        return enumber;
    }

    public void setEnumber(String enumber) {
        this.enumber = enumber;
    }

    public String getHaId() {
        return haId;
    }

    public void setHaId(String haId) {
        this.haId = haId;
    }
}
