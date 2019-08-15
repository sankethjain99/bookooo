package com.example.bookooo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.View;

import com.example.bookooo.common.common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    ViewFlipper flipper;
    Button btnSignIn,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        Paper.init(this);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent (MainActivity.this, SignUp.class);
                startActivity(signUp);

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(SignIn);
            }
        });

        int images[] = {R.drawable.a1, R.drawable.a2, R.drawable.a3};

        flipper = findViewById(R.id.flipper);

        for(int image:images){
            flipperImages(image);


            String user = Paper.book().read(common.USR_KEY);
            String password = Paper.book().read(common.PWD_KEY);
            if (user != null&& password!=null)
            {
                if (!user.isEmpty() &&!password.isEmpty())
                {
                    login(user,password);
                }
            }
        }
    }

    private void login(final String phone, final String password) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        if (common.isConnectedToInternet(getBaseContext())) {



            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("loading...");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //user check
                    if (dataSnapshot.child(phone).exists()) {
                        //get user information
                        mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(password)) {

                            Intent homeintent = new Intent(MainActivity.this, Home.class);
                            common.currentUser = user;
                            startActivity(homeintent);
                            finish();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, " Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, " User Not In Database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override


                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Check your Connection", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void flipperImages(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);

        flipper.addView(imageView);
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);


        flipper.setInAnimation(this, android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);


    }
}


