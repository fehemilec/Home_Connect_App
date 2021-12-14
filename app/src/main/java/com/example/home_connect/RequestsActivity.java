package com.example.home_connect;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import service.ApiManager;
import service.ApplianceService;
import service.Data;


public class RequestsActivity extends AppCompatActivity {

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    public  String token;
    private EditText editText,result_text;
    private Button butt;
    private ImageView micButton;
    private RequestQueue mQueue;
    public static int amount;
    private String strength = "";
    private String state = "1";
    private  CountDownTimer countDownTimer;
    private ProgressBar mProgressBar;
    private String mach_connected;
    private static String mach_status="P";
    private String sentence = "";
    private String program = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon_new);
        getSupportActionBar().setTitle("  Home Connect");


        Intent intent = getIntent();
        token = intent.getStringExtra(LoadActivity.EXTRA_CODE_TOK);


        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        //check_appliance_status(token,"BOSCH-HCS06COM1-0631A8BD1A43");


        //textView = findViewById(R.id.textView);
        editText = findViewById(R.id.text);
        //result_text = findViewById(R.id.stateval);
        micButton = findViewById(R.id.button);
        mProgressBar=findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        mQueue = Volley.newRequestQueue(this);


        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

                //    Log.d("Error: heee ",String.valueOf(i));

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_baseline_mic);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));

                sentence = data.get(0);
                String[] splitStr = sentence.split("\\s+");

                //textView.setText(splitStr[1]);
                program = "";

                strength = Coffee_strength(sentence);


                check_machine_connected(token);


            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editText.setHint("Listening...");
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.ic_baseline_mic_red);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestsActivity.this);
                alertDialogBuilder.setMessage(MainActivity.str);
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


    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("The status of the coffee machine is off, please turn it on and make another request.");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_mic_red);
        alertDialogBuilder.setTitle("Connection Failed");
        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Log.d("internet","Ok btn pressed");
                //finishAffinity();
                //System.exit(0);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public String Coffee_strength(String sentence){

        String stren = "";

        if(sentence == null || sentence.isEmpty()) return "";

        if(sentence.toLowerCase().contains("double shot plus plus")){
            stren = "DoubleShotPlusPlus";
        }
        else if(sentence.toLowerCase().contains("double shot plus")){
            stren = "DoubleShotPlus";
        }
        else if(sentence.toLowerCase().contains("double shot")){
            stren = "DoubleShot";
        }
        else if(sentence.toLowerCase().contains("very strong")){
            stren = "VeryStrong";
        }
        else if(sentence.toLowerCase().contains("strong")){
            stren = "Strong";
        }
        else if(sentence.toLowerCase().contains("very mild")){
            stren = "VeryMild";
        }
        else if(sentence.toLowerCase().contains("mild")){
            stren = "Mild";
        }


        return stren;
    }

    public static int extractNumber(String str) {

        if(str == null || str.isEmpty()) return 0;

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return Integer.valueOf(sb.toString());
    }


    String content = "";
    boolean conn ;



    public void check_machine_connected(String token){


        ApplianceService client1 = ApiManager.getClient().create(ApplianceService.class);
        Call<Data<Appliance>> appliancecall = client1.getAppliances(token);

        appliancecall.enqueue(new Callback<Data<Appliance>>() {
            @Override
            public void onResponse(Call<Data<Appliance>> call, retrofit2.Response<Data<Appliance>> response) {


                if (!response.isSuccessful()) {
                    result_text.setText("Code " + response.code());
                    return;
                }

                Data<Appliance> post = response.body();

                content = post.getData().getConnected();

                // result_text.setText(content);


                if(content.equals("true")){
                    conn = true;

                }
                else{
                    conn=false;

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestsActivity.this);
                    alertDialogBuilder.setMessage("Machine NOT connected to Internet");
                    alertDialogBuilder.setIcon(R.drawable.ic_baseline_mic_red);
                    alertDialogBuilder.setTitle("Problem reaching machine");
                    alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                if(conn){

                    check_machine_isON(token);

                }

            }

            @Override
            public void onFailure(Call<Data<Appliance>> call, Throwable t) {

                Toast.makeText(RequestsActivity.this, "FAIL", Toast.LENGTH_SHORT).show();

            }
        });


    }


    String str="";

    private void get_Appliance_Programs(String tok) {
        String url = ApiManager.BASE_URL+"/api/homeappliances/BOSCH-HCS06COM1-0631A8BD1A43/programs";
        StringBuilder sb = new StringBuilder();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject employee = response.getJSONObject("data");
                            JSONArray jsonArray = employee.getJSONArray("programs");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject emplo = jsonArray.getJSONObject(i);
                                String progr = emplo.getString("key");
                                progr = progr.substring(progr.lastIndexOf("."),progr.length());
                                progr = progr.substring(1);

                                str = sb.append(progr + "\n").toString();

                            }

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestsActivity.this);
                            alertDialogBuilder.setMessage(str.toString());
                            alertDialogBuilder.setIcon(R.drawable.ic_baseline_mic_red);
                            alertDialogBuilder.setTitle("Coffee Machine Programs");
                            alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", tok);
                return headers;
            }
        };
        mQueue.add(request);
    }


    int i=0;
    public void timer(int sec) {

        countDownTimer =new CountDownTimer(sec,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressBar.setProgress((int)i*100/(sec/1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                i=0;
                mProgressBar.setProgress(100);
                mProgressBar.setProgress(0);
                countDownTimer.cancel();

            }
        };
        countDownTimer.start();
    }

    /*----------------------
    THIS IS MAIN PROGRAM
     ----------------------*/


    public void check_machine_isON(String tok) {

        String url = ApiManager.BASE_URL+"/api/homeappliances/BOSCH-HCS06COM1-0631A8BD1A43/settings";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray jsonArray = data.getJSONArray("settings");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject emplo = jsonArray.getJSONObject(i);
                                String key = emplo.getString("key");
                                //int age = employee.getInt("age");
                                String val = emplo.getString("value");
                                //editText.append(key + ", "  + val + "\n\n");

                                if(val.substring(31).equals("On")){


                                        //-------------------------
                                        // defualt strength value
                                        //-------------------------
                                        if (strength.equals("")) {
                                            strength = "Normal";
                                        }

                                        if ((sentence.toLowerCase().contains("espresso"))
                                                && sentence.toLowerCase().contains("macchiato")) {

                                            program = "EspressoMacchiato";

                                            if (sentence.matches(".*\\d.*")) {
                                                amount = extractNumber(sentence);

                                                if (amount < 40 || amount > 60) {
                                                    Toast.makeText(RequestsActivity.this, "You can choose EspressoMacchiato size between 40 and 60 ml", Toast.LENGTH_LONG).show();
                                                } else {
                                                    start_coffee(token, program, amount, strength);
                                                    timer(30000);
                                                }
                                            } else {
                                                start_coffee(token, program, 50, strength);
                                                timer(30000);
                                            }

                                        } else if (sentence.contains("Espresso") || sentence.contains("espresso")) {

                                            program = "Espresso";

                                            if (sentence.matches(".*\\d.*")) {
                                                amount = extractNumber(sentence);

                                                if (amount < 35 || amount > 60) {
                                                    Toast.makeText(RequestsActivity.this, "You can choose Espresso size between 35 and 60 ml", Toast.LENGTH_LONG).show();
                                                } else {
                                                    start_coffee(token, program, amount, strength);
                                                    timer(26000);
                                                }
                                            } else {
                                                start_coffee(token, program, 50, strength);
                                                timer(26000);
                                            }
                                        } else if (sentence.contains("Coffee") || sentence.contains("coffee")) {
                                            program = "Coffee";

                                            if (sentence.matches(".*\\d.*")) {
                                                amount = extractNumber(sentence);

                                                if (amount < 60 || amount > 250) {
                                                    Toast.makeText(RequestsActivity.this, "You can choose Coffee size between 60 and 250 ml", Toast.LENGTH_LONG).show();
                                                } else {
                                                    start_coffee(token, program, amount, strength);
                                                    timer(10000);
                                                }
                                            } else {
                                                start_coffee(token, program, 140, strength);
                                                timer(10000);
                                            }
                                        } else if (sentence.contains("Cappuccino") || sentence.contains("cappuccino")) {
                                            program = "Cappuccino";

                                            if (sentence.matches(".*\\d.*")) {
                                                amount = extractNumber(sentence);

                                                if (amount < 100 || amount > 300) {
                                                    Toast.makeText(RequestsActivity.this, "You can choose choose Cappuccino size between 100 and 300 ml", Toast.LENGTH_LONG).show();
                                                } else {
                                                    start_coffee(token, program, amount, strength);
                                                    timer(21000);
                                                }
                                            } else {
                                                start_coffee(token, program, 200, strength);
                                                timer(21000);
                                            }
                                        } else if ((sentence.contains("Latte") || sentence.contains("latte"))
                                                && (sentence.contains("Macchiato") || sentence.contains("macchiato"))) {

                                            program = "LatteMacchiato";

                                            if (sentence.matches(".*\\d.*")) {
                                                amount = extractNumber(sentence);

                                                if (amount < 200 || amount > 400) {
                                                    Toast.makeText(RequestsActivity.this, "You can choose LatteMacchiato size between 200 and 400 ml", Toast.LENGTH_LONG).show();
                                                } else {
                                                    start_coffee(token, program, amount, strength);
                                                    timer(16000);
                                                }
                                            } else {
                                                start_coffee(token, program, 300, strength);
                                                timer(16000);
                                            }


                                        } else if ((sentence.toLowerCase().contains("show ") || sentence.toLowerCase().contains("which")
                                                || sentence.toLowerCase().contains("what")) && (sentence.toLowerCase().contains("programs") || sentence.contains("programmes")
                                                || sentence.toLowerCase().contains("coffee"))) {

                                            get_Appliance_Programs(token);
                                        } else if ((sentence.toLowerCase().contains("stop")) || (sentence.toLowerCase().contains("cancel"))) {
                                            stop_program_executing(token);
                                            mProgressBar.setProgress(0);
                                            countDownTimer.cancel();
                                            Toast.makeText(RequestsActivity.this, "Program has been stopped", Toast.LENGTH_LONG).show();
                                        }




                                    //String pg="";
                                    amount=0;
                                    //jsonParse(token);
                                }
                                else if(val.substring(31).equals("Standby")){
                                    //editText.append("\n\n Machine's state is apparently set to " + val.substring(31));

                                        popupMessage();

                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", tok);
                return headers;
            }
        };

        mQueue.add(request);


    }


    private void start_coffee(String tok, String program, int amount,String strength) {

        String url = ApiManager.BASE_URL+"/api/homeappliances/BOSCH-HCS06COM1-0631A8BD1A43/programs/active";

        final JSONObject jsonObject = new JSONObject();

        JSONObject dataobj = new JSONObject();
        //Create json array for options
        JSONArray arrayoptions = new JSONArray();

        //Create json objects for two options
        JSONObject option1 = new JSONObject();
        JSONObject option2 = new JSONObject();

        String cof = "ConsumerProducts.CoffeeMaker.Program.Beverage.";
        String bean= "ConsumerProducts.CoffeeMaker.EnumType.BeanAmount.";
        try {
            dataobj.put("key", cof+program);
            option1.put("key","ConsumerProducts.CoffeeMaker.Option.BeanAmount");
            option1.put("value",bean+strength);

            option2.put("key","ConsumerProducts.CoffeeMaker.Option.FillQuantity");
            option2.put("value",amount);
            option2.put("unit","ml");

            arrayoptions.put(option1);
            arrayoptions.put(option2);

            dataobj.put("options",arrayoptions);

            jsonObject.put("data",dataobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", tok);
                return headers;
            }
        };
        mQueue.add(request1);

    }

    private void stop_program_executing(String tok) {

        String url = ApiManager.BASE_URL+"/api/homeappliances/BOSCH-HCS06COM1-0631A8BD1A43/programs/active";
        JsonObjectRequest delete_request = new JsonObjectRequest(Request.Method.DELETE, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", tok);
                return headers;
            }
        };
        mQueue.add(delete_request);

    }

}