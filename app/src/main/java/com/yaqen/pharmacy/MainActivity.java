package com.yaqen.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Define items from (activity_main) layout
    EditText username , password;
    Button login , registration;

    // Call (Global) class
    Globalv globalv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // continuation define items from (activity_main) layout
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        registration = (Button) findViewById(R.id.reg);

        globalv = (Globalv) getApplicationContext();

        // Intent from this class to (Registration) class when click (registration) button
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , Registration.class);
                startActivity(intent);
            }
        });



    }

    // When click (login) button
    public void login(View view) {


        // define string get values from EditText
        final String user = username.getText().toString();
        final String pass = password.getText().toString();

        // disable button
        login.setEnabled(false);

        // checking if username and password exists or not
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    // key checking from api (login.php)
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    // checking if user doesn't insert any details to edittext
                    if (user.length() == 0 || pass.length() == 0) {
                        // show message to user
                        Toast.makeText(getApplicationContext(), "Enter username or password", Toast.LENGTH_LONG).show();
                        // able button
                        login.setEnabled(true);

                    }
                    // if key = (success) user exists on database
                    else if (success)

                    {
                        Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                        // save username to (Global) class
                        globalv.set_name(user);
                        Intent intent = new Intent(getApplicationContext(), pharmacy_drugs.class);
                        startActivity(intent);
                        login.setEnabled(true);
                        // finish this class
                        finish();
                    }
                    // if user doesn't exists on database show this message
                    else {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        login.setEnabled(true);

                    }


                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };

        // call (Log_in_conn) class and insert username and password to check
        Log_in_conn log_in = new Log_in_conn(user.trim(), pass, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(log_in);

        // check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


        }
        // check if internet connection failed
        else {
            login.setEnabled(true);
            Toast.makeText(getApplicationContext() , "Internet connection failed" , Toast.LENGTH_LONG).show();
        }

    }

    // class get key from api (login.php) and send values from edittext
    public class Log_in_conn extends StringRequest {
        private static final String url = "https://abdelrahmanpharmacy.000webhostapp.com/login.php";
        private Map<String , String> mapdata;

        public Log_in_conn(String username , String password , Response.Listener<String> listener ) {
            super(Method.POST , url , listener , null);
            mapdata = new HashMap<>();
            mapdata.put("username" , username);
            mapdata.put("password" , password);

        }
        @Override
        public Map <String , String> getParams(){
            return mapdata;
        }
    }





}
