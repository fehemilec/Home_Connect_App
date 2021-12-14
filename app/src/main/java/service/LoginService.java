package service;

import com.example.home_connect.ui.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {

    @Headers("Accept: application/json")
    @POST("/security/oauth/token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientid,
            @Field("client_secret") String clientsecret,
            @Field("code") String code,
            @Field("grant_type") String grant_type
    );
}
