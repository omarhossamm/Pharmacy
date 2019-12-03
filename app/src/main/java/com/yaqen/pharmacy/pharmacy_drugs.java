package com.yaqen.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pharmacy_drugs extends AppCompatActivity {

    // Define items from (activity_pharmacy_drugs) layout
    Button pharmacies , drugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_drugs);

        // continuation define items from (activity_pharmacy_drugs) layout
        pharmacies = (Button) findViewById(R.id.pharmacy);
        drugs = (Button) findViewById(R.id.drug);

        // Intent from this class to (Pharmacy) class when click (pharmacies) button
        pharmacies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , Pharmacy.class));
            }
        });


        // Intent from this class to (Search_drug) class when click (drugs) button
        drugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , Search_drug.class));
            }
        });
    }
}
