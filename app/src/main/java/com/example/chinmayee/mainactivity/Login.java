package com.example.chinmayee.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {


    Drive myapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    //    Intent myIntent = new Intent(Login.this, HomeActivity.class);
    //    Bundle b = new Bundle();
    //    b.putString("filter", query); //Your id
    //    b.putString("userLevel", userLevel);
    //    myIntent.putExtras(b); //Put your id to your next Intent
    //    startActivity(myIntent);

        final EditText edUsername  = (EditText)findViewById(R.id.edit_username);
        final EditText  edPassword  = (EditText)findViewById(R.id.edit_password);
        Button btnValidate = (Button)findViewById(R.id.loginButton);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String uname = edUsername.getText().toString();
                String pass = edPassword.getText().toString();
                if (uname.equals("demo@husky.neu.edu") && pass.equals("demo1")) {
                    Intent intent = new Intent(Login.this, HomeActivity.class);
                  //  intent.putExtra("username", edUsername.getText().toString());
                    myapp = (Drive) getApplication();
                    myapp.setUserId("001722744");
                    myapp.setUserEmail(uname);
                    myapp.setLevel("1");
                    myapp.setPic("profileimg1");
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Invalid username and password pair. Please check!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button register = (Button)findViewById(R.id.new_user);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });



    }

}
