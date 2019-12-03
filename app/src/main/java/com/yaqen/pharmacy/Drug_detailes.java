package com.yaqen.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class Drug_detailes extends AppCompatActivity {

    String url = "https://abdelrahmanpharmacy.000webhostapp.com/drug_detailes.php";
    ListView listView;
    ArrayList<drug_details> items = new ArrayList<drug_details>();

    public String drugid , pharmacyname , drugname , druglogo , price ,
            quantity , DrugAndPharmaceID , namee , key;

    Globalv globalv;

    AlertDialog dialog;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detailes);
        globalv = (Globalv) getApplicationContext();



        namee = globalv.getname();

        Intent intent = getIntent();

        key = intent.getExtras().getString("drug_name");



        listView = (ListView) findViewById(R.id.list);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        drugid = object.getString("DrugID");
                        DrugAndPharmaceID = object.getString("DrugAndPharmaceID");
                        pharmacyname = object.getString("PharmacyName");
                        drugname = object.getString("DrugName");
                        druglogo = object.getString("DrugLogo");
                        price = object.getString("price");
                        quantity = object.getString("quantity");


                          items.add(new drug_details(drugid , DrugAndPharmaceID , pharmacyname,
                                  drugname,druglogo,price,quantity));


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listData();

            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("drugname", key);


                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest);






    }





    public void listData(){
        listview adapter = new listview(items);
        listView.setAdapter(adapter);
    }


    class listview extends BaseAdapter {
        TextView available , quantity;
        // connect array list by c_info data
        ArrayList<drug_details> item = new ArrayList<drug_details>();


        // constructor
        listview(ArrayList<drug_details> item){
            this.item = item ;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position).drugid;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // open name_c layout from this activity
            LayoutInflater inflater = getLayoutInflater();



                View view = inflater.inflate(R.layout.search_drug_item, null);


                // decleare content of layout

                TextView name = (TextView) view.findViewById(R.id.drug_name);
                ImageView logo = (ImageView) view.findViewById(R.id.drug_logo);
                TextView pharmacy_name = (TextView) view.findViewById(R.id.pharmacy_name);
                quantity = (TextView) view.findViewById(R.id.quantity);
                TextView price = (TextView) view.findViewById(R.id.price);

            name.append(item.get(position).drugname);
            Picasso.with(getApplicationContext()).load("https://abdelrahmanpharmacy.000webhostapp.com/images/" + item.get(position).druglogo).into(logo);
            pharmacy_name.append(item.get(position).pharmacyname);
            quantity.append(item.get(position).quantity);
            price.append(item.get(position).price + " EGP");

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Drug_detailes.this);


                    View vieww = getLayoutInflater().inflate(R.layout.custom_dialog , null);


                    available = (TextView) vieww.findViewById(R.id.avaliable);
                    final EditText buy_available = (EditText) vieww.findViewById(R.id.buy_edit);
                    Button buy = (Button) vieww.findViewById(R.id.buy_button);

                    available.append(item.get(i).quantity + " Packet");

                    buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (Integer.parseInt(buy_available.getText().toString()) >
                                    Integer.parseInt(item.get(i).quantity)) {
                                Toast.makeText(getApplicationContext(), "Don't allow", Toast.LENGTH_LONG).show();
                            }


                            else {
                                Response.Listener<String> responseListner = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            boolean success = jsonObject.getBoolean("success");


                                            if (success) {

                                                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();

                                                dialog.cancel();
                                                finish();
                                                Intent intent = getIntent();
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Internet connection failed", Toast.LENGTH_LONG).show();


                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };


                                recieve_drug send_data = new recieve_drug(item.get(i).DrugAndPharmaceID
                                        , buy_available.getText().toString(), namee, responseListner);
                                RequestQueue requestQueue = Volley.newRequestQueue(Drug_detailes.this);
                                requestQueue.add(send_data);
                            }
                        }});

                    builder.setView(vieww)
                            .setTitle("Buy this item");


                    dialog = builder.create();
                    dialog.show();

                }
            });




            return view;
        }
    }



    public class recieve_drug extends StringRequest {
        static final String send_data_url = "https://abdelrahmanpharmacy.000webhostapp.com/buy_drug.php" ;
        private Map<String, String > MapData;
        public recieve_drug (String DrugAndPharmaceID  , String quantity , String name
                , Response.Listener<String> listener){
            super(Method.POST , send_data_url , listener , null);
            MapData = new HashMap<>();

            MapData.put("DrugAndPharmaceID" , DrugAndPharmaceID);
            MapData.put("quantity" , quantity);
            MapData.put("name" , name);

        }
        @Override
        public Map  <String , String> getParams(){
            return MapData;
        }

    }

    public class drug_details {
        public String drugid;
        public String DrugAndPharmaceID;
        public String pharmacyname;
        public String drugname;
        public String druglogo;
        public String price;
        public String quantity;

                public drug_details(String drugid , String DrugAndPharmaceID ,String pharmacyname , String drugname ,
                                    String druglogo , String price , String quantity) {
                    this.drugid = drugid;
                    this.DrugAndPharmaceID = DrugAndPharmaceID;
                    this.pharmacyname = pharmacyname;
                    this.drugname = drugname;
                    this.druglogo = druglogo;
                    this.price = price;
                    this.quantity = quantity;

                }

    }


}
