package service;

import com.example.home_connect.Appliance;
import com.example.home_connect.ui.AccessToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApplianceService {

    @Headers("Accept: application/vnd.bsh.sdk.v1+json")
    @GET("/api/homeappliances/BOSCH-HCS06COM1-0631A8BD1A43")
    Call<Data<Appliance>> getAppliances(
            @Header("Authorization") String token
    );



}
//BOSCH-HCS06COM1-0631A8BD1A43