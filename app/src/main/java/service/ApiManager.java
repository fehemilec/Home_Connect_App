package service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    public static final String clientid ="CB9F8CF2CCFE7CCBF67DA10903A22C7AB4649FEB6B120FA553AB3D4766AC436D";
    public static final String clientsecret ="6EAAEC8E730DAA3F251E62397D7B0EB36D00B71DAA9B80E9914119993655C188";
    public static final String redirectUri ="https://apiclient.home-connect.com/o2c.html";

    public static final String BASE_URL = "https://simulator.home-connect.com";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public String getClientid() {
        return clientid;
    }

    public String getClientsecret() {
        return clientsecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }


}
