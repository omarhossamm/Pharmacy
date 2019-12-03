package com.yaqen.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends AppCompatActivity {

    // Define items from (activity_registration) layout
    EditText username, password, con_password, fullname, address, mobile_phone ;
    Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // continuation define items from (activity_registration) layout
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pass);
        con_password = (EditText) findViewById(R.id.con_pass);
        fullname = (EditText) findViewById(R.id.fullname);
        address = (EditText) findViewById(R.id.address);
        mobile_phone = (EditText) findViewById(R.id.mob_phone);
        registration = (Button) findViewById(R.id.reg);

    }

    // When click (registration) button
    public void registration(View view) {

        // disable button
        registration.setEnabled(false);


        // define string get values from EditText
        String usernamee = username.getText().toString();
        String passwordd = password.getText().toString();
        String con_passwordd = con_password.getText().toString();
        String fullnamee = fullname.getText().toString();
        String addresss = address.getText().toString();
        String mobile_phonee = mobile_phone.getText().toString();



        // checking if user doesn't insert any details to edittext
        if (usernamee.length() == 0 || passwordd.length() == 0 || fullnamee.length() == 0
                || addresss.length() == 0 || mobile_phonee.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            registration.setEnabled(true);

        }
        // checking if password less than 5 character
        else if (passwordd.length() < 5) {
            Toast.makeText(getApplicationContext(), "Password very weak", Toast.LENGTH_LONG).show();
            registration.setEnabled(true);
        // checking if edittext (password) and edittext (con_password) not match
        } else if (!passwordd.equals(con_passwordd)) {
            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();
            registration.setEnabled(true);

        }

        // Registration new account in database in mysql
        else {
            Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            Toast.makeText(Registration.this, "Registration successfully", Toast.LENGTH_LONG).show();
                            registration.setEnabled(true);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Registration.this, "Username already exists", Toast.LENGTH_LONG).show();
                            registration.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            };

            // call (regist) class and insert details to create new account
            regist send_data = new regist(usernamee , passwordd, fullnamee , addresss , mobile_phonee
                    , responseListner);
            RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
            requestQueue.add(send_data);



        }
        // check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


        }
        // check if internet connection failed
        else {
            registration.setEnabled(true);
            Toast.makeText(getApplicationContext() , "Internet connection failed" , Toast.LENGTH_LONG).show();
        }

    }


    // class get key from api (login.php) and send values from edittext
    public class regist extends StringRequest {
        static final String send_data_url = "https://abdelrahmanpharmacy.000webhostapp.com/registration.php" ;
        private Map<String, String > MapData ;
        public regist (String username , String passwordd , String fullname
                , String address
                , String mobile_phone , Response.Listener<String> listener){
            super(Method.POST , send_data_url , listener , null);
            MapData = new HashMap<>();
            MapData.put("username" , username);
            MapData.put("password" , passwordd);
            MapData.put("fullname" , fullname);
            MapData.put("address" , address);
            MapData.put("mobile_phone" , mobile_phone);

        }
        @Override
        public Map<String, String> getParams(){
            return MapData;
        }

    }



}
