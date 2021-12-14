package com.example.home_connect.ui;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("refresh_token")
    private String refreshtoken;

    @SerializedName("expires_in")
    private String expire;


    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshtoken;
    }

    public String getExpireToken() {
        return expire;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if (! Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                    Character
                            .toString(tokenType.charAt(0))
                            .toUpperCase() + tokenType.substring(1);
        }

        return tokenType;
    }
}