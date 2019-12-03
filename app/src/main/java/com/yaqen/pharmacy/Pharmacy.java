package com.yaqen.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pharmacy extends AppCompatActivity {

    // declare variables
    String url = "https://abdelrahmanpharmacy.000webhostapp.com/pharmasies.php";
    ListView listVieww;
    ArrayList<pharmacy_detailes> items = new ArrayList<pharmacy_detailes>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        // continuation define items from (activity_pharmacy) layout
        listVieww = (ListView) findViewById(R.id.listview);

        // call function
        pharmacies();





    }


    // set adapter (listview) class extends BaseAdapter
    public void listData(){
        listview adapter = new listview(items);
        listVieww.setAdapter(adapter);
    }


    // function to get pharmacies names and logo from api
    public void pharmacies(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    // get json array length and add id , name , logo for each i position
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        String id = object.getString("PharmacyID");
                        String name = object.getString("PharmacyName");
                        String logo = object.getString("PharmacyLogo");

                        // add variables to (pharmacy_detailes) class
                        items.add(new pharmacy_detailes(id , name , logo));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // call listData function
                listData();
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest);


    }

    // class to handle listview
    class listview extends BaseAdapter {

        // connect arraylist by pharmacy_detailes class
        ArrayList<pharmacy_detailes> item = new ArrayList<pharmacy_detailes>();


        // constructor
        listview(ArrayList<pharmacy_detailes> item){
            this.item = item ;
        }


        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position).name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // open pharmacy_item layout from this activity
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.pharmacy_item , null);


            // Define items from (pharmacy_item) layout

            TextView name = (TextView) view.findViewById(R.id.pharmacy_name);
            ImageView logo = (ImageView) view.findViewById(R.id.pharmacy_logo);

            // set each string from arraylist position name , logo
            name.setText(item.get(position).name);
            // picasso library that get image from any server
            Picasso.with(getApplicationContext()).load("https://abdelrahmanpharmacy.000webhostapp.com/images/" + item.get(position).logo).into(logo);

            // Intent from this class to (Drugs) class when click (name) TextView
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext() , Drugs.class);
                    // put variable that we need in Drugs.class by key ("id")
                    intent.putExtra("id" , items.get(position).id);
                    startActivity(intent);

                }
            });


            return view;
        }
    }


    // class to add details from pharmacies function to strings in this class
    public class pharmacy_detailes {
        public String id;
        public String name;
        public String logo;

        public pharmacy_detailes(String id ,String name , String logo) {
            this.id = id;
            this.name = name;
            this.logo = logo;

        }
    }



}
