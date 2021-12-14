package service;

import com.example.home_connect.Appliance;
import com.google.gson.annotations.SerializedName;

public class Data<T> {

    @SerializedName("data")
    private Appliance data;

    public Appliance getData() {
        return data;
    }


}



