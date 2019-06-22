package com.example.bookooo;

import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;


public class SignUp extends AppCompatActivity {


    private Spinner spinner;
   private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));

        editText = findViewById(R.id.editPhone);

        findViewById(R.id.btnVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = editText.getText().toString().trim();
                if(number.isEmpty()|| number.length()>10){
                    editText.setError(" valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phonenumber= "+" + code + number;

                Intent intent = new Intent(SignUp.this,activity_verify_phone.class);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);




            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(this, ProfileData.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }
    }
}
