package com.example.home_connect;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.home_connect.ui.AccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ApiManager;
import service.LoginService;

public class LoadActivity extends AppCompatActivity {

    private String token;
    private String code;
    public static final String EXTRA_CODE_TOK = "com.example.home_connect.EXTRA_CODE_TOK";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);


        Intent intent = getIntent();
        code = intent.getStringExtra(MainActivity.EXTRA_CODE);

        //send code and get token

        LoginService client = ApiManager.getClient().create(LoginService.class);
        Call<AccessToken> accessTokenCall = client.getAccessToken(
                ApiManager.clientid,
                ApiManager.clientsecret,
                code,
                "authorization_code"
        );


        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                token = response.body().getAccessToken().toString();
              //  refreshtoken = response.body().getRefreshToken().toString();
                //expire = response.body().getExpireToken().toString();


                //Toast.makeText(MainActivity.this, expire, Toast.LENGTH_SHORT).show();

                openRequestActivity(token);
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Toast.makeText(LoadActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void openRequestActivity(String tok) {

        Intent intent = new Intent(this,RequestsActivity.class);
        intent.putExtra(EXTRA_CODE_TOK,tok);
        startActivity(intent);

    }
}