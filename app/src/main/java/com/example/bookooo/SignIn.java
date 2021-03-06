package com.example.bookooo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookooo.common.common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;
    CheckBox cbkRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        cbkRemember = (CheckBox)findViewById(R.id.ckbRemember);


        Paper.init(this);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (common.isConnectedToInternet(getBaseContext())) {
                    if (cbkRemember.isChecked())
                    {
                        Paper.book().write(common.USR_KEY,edtPhone.getText().toString());
                        Paper.book().write(common.PWD_KEY,edtPassword.getText().toString());
                    }


                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("loading...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //user check
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                //get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString());
                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Intent homeintent = new Intent(SignIn.this, Home.class);
                                    common.currentUser = user;
                                    startActivity(homeintent);
                                    finish();
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, " Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, " User Not In Database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override


                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }
                    });
                }
                else{
                    Toast.makeText(SignIn.this, "Check your Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
    }
}
