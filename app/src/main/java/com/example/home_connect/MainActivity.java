package com.example.home_connect;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.home_connect.ui.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ApiManager;
import service.LoginService;


public class MainActivity extends AppCompatActivity {

    private String code = "";
    public static final String EXTRA_CODE = "com.example.home_connect.EXTRA_CODE";
    public static final String str = "• This app is developed to control and monitor the enabled Coffee machine. \n" +
            "• To use it, username and password should be inserted correctly.\n" +
            "• After successful login, a coffee machine and microphone will be displayed.\n" +
            "• Check if the coffee machine is ON before using it.\n" +
            "• After clicking the microphone, user can immediately start requesting for coffee.\n" +
            "• Available coffee types are \n" +
            "o Espresso\n" +
            "o Espresso Macchiato\n" +
            "o Coffee\n" +
            "o Cappuccino\n" +
            "o Latte Macchiato\n" +
            "o Caffe Latte\n" +
            "• The yellow colour in gray bar on machine shows the processing of requested coffee.\n" +
            "• User can request for quantity of coffee through voice command.\n" +
            "• The minimum available quantity for Espresso, Espresso Macchiato, Coffee, Cappuccino, Latte \n" +
            "Macchiato, Caffe Latte are 35ml, 40ml, 60ml, 100ml, 200ml, 100ml respectively.\n" +
            "• The maximum available quantity for Espresso, Espresso Macchiato, Coffee, Cappuccino, \n" +
            "Latte Macchiato, Caffe Latte are 60ml, 60ml, 250ml, 300ml, 400ml, 400ml respectively.\n" +
            "• Default quantity will be selected in the case of no specification.\n" +
            "• User can specify the strength of coffee through voice command.\n" +
            "• Available coffee strengths are\n" +
            "o Very Mild\n" +
            "o Normal\n" +
            "o Mild\n" +
            "o Strong\n" +
            "o Very Strong\n" +
            "o Double Shot+\n" +
            "o Double Shot\n" +
            "o Double Shot++\n" +
            "• Default coffee strength will be selected in the case of no specification\n"+
            "• To stop or cancel any request, user should say STOP or Cancel.\n" +
            "• Examples of some basic voice command:\n" +
            "o Show me all the programmes.\n" +
            "o I want to have an Espresso.\n" +
            "o Stop the program / Cancel the program.\n" +
            "o Please make a 160ml of cappuccino.\n" +
            "o Prepare a Very Strong Coffee.\n" +
            "o I want a 120ml of Mild Caffe Latte.\n" +
            "o A 60ml of Double Shot++ Late Macchiato please.\n" +
            "o Make a Normal Espresso Macchiato.";

    private String refreshtoken;
    private String expire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon_new);
        getSupportActionBar().setTitle("  Home Connect");

        Button loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://simulator.home-connect.com/security/oauth/authorize"+
                                "?client_id="+ ApiManager.clientid +"&response_type=code"));
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_info:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage(str.toString());
                alertDialogBuilder.setIcon(R.mipmap.logo);
                alertDialogBuilder.setTitle("Coffee Machine Instructions");
                alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.navigation_exit:
                this.finishAffinity();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();


        if(uri != null) {
            code = uri.getQueryParameter("code");

            openLoadActivity(code);
        }



    }

    private void openRequestActivity(String tok) {

        Intent intent = new Intent(this,RequestsActivity.class);
        intent.putExtra(EXTRA_CODE,tok);
        startActivity(intent);

    }

    private void openLoadActivity(String cod) {

        Intent intent = new Intent(this,LoadActivity.class);
        intent.putExtra(EXTRA_CODE,cod);
        startActivity(intent);

    }

}