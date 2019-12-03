package com.yaqen.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Search_drug extends AppCompatActivity {

    String url = "https://abdelrahmanpharmacy.000webhostapp.com/alldrugs.php";
    ListView listView;

    ArrayList<String> items = new ArrayList<String>();
    public String drugname;

    SearchView search_drug;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);

        search_drug = (SearchView) findViewById(R.id.search_drug);

        listView = (ListView) findViewById(R.id.list);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        drugname = object.getString("DrugName");

                        items.add(new String(drugname));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                arrayAdapter = new ArrayAdapter<String>
                        (Search_drug.this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(arrayAdapter);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest);


        // filter listview when write any char
        search_drug.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;

            }

            @Override
            public boolean onQueryTextChange(final String s) {
                arrayAdapter.getFilter().filter(s);


                return true;
            }
        });





        // when click on any item of listview intent to Drug_detailes.class with drug name variable by key ("drug_name")
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext() , Drug_detailes.class);
                intent.putExtra("drug_name" , adapterView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });


    }




}
